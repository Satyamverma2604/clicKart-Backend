package com.clicKart.admin.dto;



import com.clicKart.common.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusRequest {

    @NotNull(message = "Order status is required")
    private OrderStatus status;
}