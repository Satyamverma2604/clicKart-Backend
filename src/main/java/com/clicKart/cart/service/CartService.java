
package com.clicKart.cart.service;

import com.clicKart.cart.dto.CartItemRequest;
import com.clicKart.cart.dto.CartResponse;

public interface CartService {

    CartResponse getCart(Long userId);

    CartResponse addItem(Long userId, CartItemRequest request);

    CartResponse updateItem(Long userId, Long productId, Integer quantity);

    void removeItem(Long userId, Long productId);

    void clearCart(Long userId);
}

