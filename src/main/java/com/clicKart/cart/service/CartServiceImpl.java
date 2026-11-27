
package com.clicKart.cart.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.cart.dto.CartItemRequest;
import com.clicKart.cart.dto.CartItemResponse;
import com.clicKart.cart.dto.CartResponse;
import com.clicKart.cart.entity.Cart;
import com.clicKart.cart.entity.CartItem;
import com.clicKart.cart.repository.CartItemRepository;
import com.clicKart.cart.repository.CartRepository;
import com.clicKart.exception.BadRequestException;
import com.clicKart.exception.InvalidOperationException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.product.entity.Product;
import com.clicKart.product.repository.ProductRepository;
import com.clicKart.user.entity.User;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public CartResponse getCart(Long userId) {

		Cart cart = getCartByIdOrThrow(userId);

		return mapToResponse(cart);
	}

	@Override
	@Transactional
	public CartResponse addItem(Long userId, CartItemRequest request) {

		User user = getUserByIdOrThrow(userId);

		Product product = getProductByIdOrThrow(request.getProductId());

		if (!product.getActive()) {
			throw new InvalidOperationException("Product is not available");
		}

		if (request.getQuantity() == null || request.getQuantity() <= 0) {
			throw new BadRequestException("Quantity must be greater than zero");
		}

		if (request.getQuantity() > product.getStockQuantity()) {
			throw new InvalidOperationException("Insufficient stock for product '" + product.getName()
					+ "'. Available: " + product.getStockQuantity());
		}

		Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			return cartRepository.save(newCart);
		});

		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId()).orElse(null);

		if (cartItem != null) {

			int newQuantity = cartItem.getQuantity() + request.getQuantity();

			if (newQuantity > product.getStockQuantity()) {
				throw new InvalidOperationException("Insufficient stock");
			}

			cartItem.setQuantity(newQuantity);
			cartItem.setPrice(product.getPrice());

		} else {

			cartItem = new CartItem();

			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(request.getQuantity());
			cartItem.setPrice(product.getPrice());

			cart.addItem(cartItem);
		}

		cartItemRepository.save(cartItem);

		return mapToResponse(cart);
	}

	@Override
	@Transactional
	public CartResponse updateItem(Long userId, Long productId, Integer quantity) {

		Cart cart = getCartByIdOrThrow(userId);

		Product product = getProductByIdOrThrow(productId);

		if (!product.getActive()) {
			throw new InvalidOperationException("Product is not available");
		}

		if (quantity == null || quantity <= 0) {
			throw new BadRequestException("Quantity must be greater than zero");
		}

		if (quantity > product.getStockQuantity()) {
			throw new InvalidOperationException("Insufficient stock for product '" + product.getName()
					+ "'. Available: " + product.getStockQuantity());
		}

		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		cartItem.setQuantity(quantity);
		cartItem.setPrice(product.getPrice());

		cartItemRepository.save(cartItem);

		return mapToResponse(cart);
	}

	@Override
	@Transactional
	public void removeItem(Long userId, Long productId) {

		Cart cart = getCartByIdOrThrow(userId);

		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		cart.removeItem(cartItem);

	}

	@Override
	@Transactional
	public void clearCart(Long userId) {

		Cart cart = getCartByIdOrThrow(userId);

		cart.getItems().clear();

		cartItemRepository.deleteByCartId(cart.getId());
	}

	/*******************************************
	 * HELPER METHODS *
	 *******************************************/

	private CartResponse mapToResponse(Cart cart) {

		List<CartItemResponse> items = cart.getItems().stream().map(this::mapToItemResponse).toList();

		BigDecimal totalAmount = items.stream().map(CartItemResponse::getTotalPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		CartResponse response = new CartResponse();

		response.setId(cart.getId());
		response.setItems(items);
		response.setTotalAmount(totalAmount);

		return response;
	}

	private CartItemResponse mapToItemResponse(CartItem item) {

		CartItemResponse response = new CartItemResponse();

		response.setId(item.getId());
		response.setProductId(item.getProduct().getId());
		response.setProductName(item.getProduct().getName());
		response.setImageUrl(item.getProduct().getImageUrl());
		response.setQuantity(item.getQuantity());
		response.setPrice(item.getPrice());
		response.setTotalPrice(item.getTotalPrice());

		return response;
	}

	private Product getProductByIdOrThrow(Long id) {
		// TODO Check Product exists or not
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	private Cart getCartByIdOrThrow(Long id) {
		// TODO Check Cart exists or not
		return cartRepository.findByUserId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + id));
	}

	private User getUserByIdOrThrow(Long id) {
		// TODO Check User exists or not
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
	}

}
