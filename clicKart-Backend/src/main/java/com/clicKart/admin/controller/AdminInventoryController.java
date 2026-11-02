
package com.clicKart.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clicKart.admin.dto.InventoryResponse;
import com.clicKart.admin.dto.StockUpdateRequest;
import com.clicKart.admin.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getInventory() {

        return ResponseEntity.ok(
        		inventoryService.getInventory()
        );
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponse>> getLowStockProducts() {

        return ResponseEntity.ok(
        		inventoryService.getLowStockProducts()
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateStock(
            @PathVariable Long productId,
            @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
        		inventoryService.updateStock(
                        productId,
                        request.getQuantity()
                )
        );
    }
}

