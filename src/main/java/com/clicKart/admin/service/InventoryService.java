package com.clicKart.admin.service;

import java.util.List;

import com.clicKart.admin.dto.InventoryResponse;

public interface InventoryService {

    List<InventoryResponse> getInventory();

    List<InventoryResponse> getLowStockProducts();

    InventoryResponse updateStock(Long productId, Integer quantity);
}