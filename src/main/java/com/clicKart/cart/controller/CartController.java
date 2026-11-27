package com.clicKart.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.clicKart.cart.dto.CartItemRequest;
import com.clicKart.cart.dto.CartResponse;
import com.clicKart.cart.service.CartService;
import com.clicKart.security.principal.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                cartService.getCart(principal.getId())
        );
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CartItemRequest request) {

        CartResponse response =
                cartService.addItem(
                        principal.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.updateItem(
                        principal.getId(),
                        productId,
                        quantity
                )
        );
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long productId) {

        cartService.removeItem(
                principal.getId(),
                productId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal UserPrincipal principal) {

        cartService.clearCart(principal.getId());

        return ResponseEntity.noContent().build();
    }
}