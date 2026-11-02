package com.clicKart.product.dto;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private String brand;

    private String imageUrl;

    private Boolean active;

    private Long categoryId;

    private String categoryName;
}