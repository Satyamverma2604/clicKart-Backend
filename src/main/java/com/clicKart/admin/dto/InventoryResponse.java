package com.clicKart.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Long productId;
    private String productName;
    private Integer stockQuantity;
    private Boolean lowStock;
}
