package com.clicKart.order.service;

import java.util.List;

import com.clicKart.common.OrderStatus;
import com.clicKart.common.PaymentStatus;
import com.clicKart.order.dto.OrderRequest;
import com.clicKart.order.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(Long userId, OrderRequest request);

    List<OrderResponse> getUserOrders(Long userId);

    OrderResponse getOrderById(Long userId, Long orderId);

    void cancelOrder(Long userId, Long orderId);
    
    //Admin methods
    
    
    
    List<OrderResponse> getAllOrders();
    
    OrderResponse getOrderById(Long orderId);

    OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus status);

    OrderResponse updatePaymentStatus(
            Long orderId,
            PaymentStatus paymentStatus);

    OrderResponse adminCancelOrder(Long orderId);
}