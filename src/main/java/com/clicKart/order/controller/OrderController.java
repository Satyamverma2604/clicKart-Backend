package com.clicKart.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.clicKart.order.dto.OrderRequest;
import com.clicKart.order.dto.OrderResponse;
import com.clicKart.order.service.OrderService;
import com.clicKart.security.principal.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Create Order
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response =
                orderService.createOrder(
                        principal.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all orders of logged-in user
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                orderService.getUserOrders(
                        principal.getId()
                )
        );
    }

    // Get single order
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(
                        principal.getId(),
                        orderId
                )
        );
    }

    // Cancel order
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId) {

        orderService.cancelOrder(
                principal.getId(),
                orderId
        );

        return ResponseEntity.noContent().build();
    }
}