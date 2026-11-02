package com.clicKart.order.dto;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clicKart.common.OrderStatus;
import com.clicKart.common.PaymentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private Long userId;

    private Long shippingAddressId;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private PaymentStatus paymentStatus;

    private List<OrderItemResponse> items = new ArrayList<OrderItemResponse>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
