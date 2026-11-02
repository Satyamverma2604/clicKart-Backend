package com.clicKart.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.address.entity.Address;
import com.clicKart.address.repository.AddressRepository;
import com.clicKart.cart.entity.Cart;
import com.clicKart.cart.entity.CartItem;
import com.clicKart.cart.repository.CartRepository;
import com.clicKart.common.OrderStatus;
import com.clicKart.common.PaymentStatus;
import com.clicKart.exception.BadRequestException;
import com.clicKart.exception.InvalidOperationException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.exception.ResourceOwnershipException;
import com.clicKart.order.dto.OrderItemResponse;
import com.clicKart.order.dto.OrderRequest;
import com.clicKart.order.dto.OrderResponse;
import com.clicKart.order.entity.Order;
import com.clicKart.order.entity.OrderItem;
import com.clicKart.order.repository.OrderRepository;
import com.clicKart.product.entity.Product;
import com.clicKart.user.entity.User;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final CartRepository cartRepository;

	@Override
	@Transactional
	public OrderResponse createOrder(Long userId, OrderRequest request) {

		// 1. Find user
		User user = getUserByIdOrThrow(userId);
		// 2. Find shipping address
		Address address = getAddressByIdOrThrow(request.getShippingAddressId());

		// 3. Make sure address belongs to this user
		if (!address.getUser().getId().equals(userId)) {
			throw new ResourceOwnershipException("Address does not belong to user");
		}

		// 4. Find user's cart
		Cart cart = getCartByUserIdOrThrow(userId);

		// 5. Check cart is not empty
		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cart is empty");
		}

		// 6. Create Order
		Order order = new Order();

		order.setUser(user);
		order.setShippingAddress(address);
		order.setOrderDate(LocalDateTime.now());

		order.setStatus(OrderStatus.PENDING);
		order.setPaymentStatus(PaymentStatus.PENDING);

		// 7. Calculate total and create OrderItems
		BigDecimal totalAmount = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getItems()) {

			Product product = cartItem.getProduct();
			Integer quantity = cartItem.getQuantity();
			BigDecimal price = cartItem.getPrice();

			// Check whether product is still active
			if (!product.getActive()) {
				throw new BadRequestException("Product is not available: " + product.getName());
			}

			// Check current stock before creating order
			if (quantity > product.getStockQuantity()) {
				throw new BadRequestException("Insufficient stock for product: " + product.getName());
			}

			BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(product);
			orderItem.setQuantity(quantity);
			orderItem.setPrice(price);
			orderItem.setSubtotal(subtotal);

			order.addItem(orderItem);

			// Reduce product stock
			product.setStockQuantity(product.getStockQuantity() - quantity);

			totalAmount = totalAmount.add(subtotal);
		}
		// 8. Set final order total
		order.setTotalAmount(totalAmount);

		// 9. Save order
		Order savedOrder = orderRepository.save(order);

		// 10. Clear cart after successful order creation
		cart.getItems().clear();

		return mapToResponse(savedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getUserOrders(Long userId) {

		if (!userRepository.existsById(userId)) {
			throw new ResourceNotFoundException("User not found");
		}

		return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::mapToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(Long userId, Long orderId) {

		Order order = getOrderByIdOrThrow(orderId);

		// Make sure user can access only their own order
		if (!order.getUser().getId().equals(userId)) {
			throw new ResourceOwnershipException("Order does not belong to user");
		}

		return mapToResponse(order);
	}

	@Override
	@Transactional
	public void cancelOrder(Long userId, Long orderId) {

		Order order = getOrderByIdOrThrow(orderId);

		// Make sure order belongs to user
		if (!order.getUser().getId().equals(userId)) {
			throw new ResourceOwnershipException("Order does not belong to user");
		}

		// Only pending/confirmed orders can be cancelled
		if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {

			throw new InvalidOperationException("Order cannot be cancelled at this stage");
		}

		// Restore product stock
		for (OrderItem orderItem : order.getItems()) {

			Product product = orderItem.getProduct();

			product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
		}

		order.setStatus(OrderStatus.CANCELLED);

		orderRepository.save(order);
	}

	// Admin methods

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getAllOrders() {

		return orderRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(Long orderId) {

		Order order = getOrderByIdOrThrow(orderId);

		return mapToResponse(order);
	}
	
	@Override
	@Transactional
	public OrderResponse updateOrderStatus(
	        Long orderId,
	        OrderStatus status) {

	    Order order = getOrderByIdOrThrow(orderId);

	    if (status == null) {
	        throw new BadRequestException(
	                "Order status is required");
	    }

	    if (order.getStatus() == OrderStatus.CANCELLED) {
	        throw new InvalidOperationException(
	                "Cancelled order status cannot be changed");
	    }

	    if (order.getStatus() == OrderStatus.DELIVERED) {
	        throw new InvalidOperationException(
	                "Delivered order status cannot be changed");
	    }

	    order.setStatus(status);

	    return mapToResponse(orderRepository.save(order));
	}
	
	@Override
	@Transactional
	public OrderResponse updatePaymentStatus(
	        Long orderId,
	        PaymentStatus paymentStatus) {

	    Order order = getOrderByIdOrThrow(orderId);

	    if (paymentStatus == null) {
	        throw new BadRequestException(
	                "Payment status is required");
	    }

	    order.setPaymentStatus(paymentStatus);

	    return mapToResponse(orderRepository.save(order));
	}
	
	@Override
	@Transactional
	public OrderResponse adminCancelOrder(Long orderId) {

	    Order order = getOrderByIdOrThrow(orderId);

	    if (order.getStatus() == OrderStatus.CANCELLED) {
	        throw new InvalidOperationException(
	                "Order is already cancelled");
	    }

	    if (order.getStatus() == OrderStatus.DELIVERED) {
	        throw new InvalidOperationException(
	                "Delivered order cannot be cancelled");
	    }

	    if (order.getStatus() == OrderStatus.SHIPPED) {
	        throw new InvalidOperationException(
	                "Shipped order cannot be cancelled");
	    }

	    // Restore stock
	    for (OrderItem item : order.getItems()) {

	        Product product = item.getProduct();

	        product.setStockQuantity(
	                product.getStockQuantity()
	                        + item.getQuantity()
	        );
	    }

	    order.setStatus(OrderStatus.CANCELLED);

	    return mapToResponse(orderRepository.save(order));
	}

	/*******************************************
	 * HELPER METHODS *
	 *******************************************/

	private Address getAddressByIdOrThrow(Long id) {

		return addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
	}

	private Cart getCartByUserIdOrThrow(Long id) {

		return cartRepository.findByUserId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + id));
	}

	private User getUserByIdOrThrow(Long id) {

		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
	}

	private Order getOrderByIdOrThrow(Long id) {

		return orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
	}

	private OrderResponse mapToResponse(Order order) {

		List<OrderItemResponse> items = order.getItems().stream().map(this::mapToItemResponse).toList();

		OrderResponse response = new OrderResponse();

		response.setId(order.getId());
		response.setUserId(order.getUser().getId());
		response.setShippingAddressId(order.getShippingAddress().getId());
		response.setOrderDate(order.getOrderDate());
		response.setStatus(order.getStatus());
		response.setTotalAmount(order.getTotalAmount());
		response.setPaymentStatus(order.getPaymentStatus());
		response.setItems(items);
		response.setCreatedAt(order.getCreatedAt());
		response.setUpdatedAt(order.getUpdatedAt());

		return response;
	}

	private OrderItemResponse mapToItemResponse(OrderItem item) {

		OrderItemResponse response = new OrderItemResponse();

		response.setId(item.getId());
		response.setProductId(item.getProduct().getId());
		response.setProductName(item.getProduct().getName());
		response.setQuantity(item.getQuantity());
		response.setPrice(item.getPrice());
		response.setSubtotal(item.getSubtotal());

		return response;
	}

}