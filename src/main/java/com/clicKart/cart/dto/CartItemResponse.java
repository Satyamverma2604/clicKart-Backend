package com.clicKart.cart.dto;



import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;
}
