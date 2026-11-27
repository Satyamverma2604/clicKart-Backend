package com.clicKart.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.admin.dto.InventoryResponse;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.product.entity.Product;
import com.clicKart.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    @Override
    public List<InventoryResponse> getInventory() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToInventoryResponse)
                .toList();
    }

    @Override
    public List<InventoryResponse> getLowStockProducts() {

        // We will implement the low-stock logic here.
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getStockQuantity() <= 10)
                .map(this::mapToInventoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryResponse updateStock(
            Long productId,
            Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId));

        product.setStockQuantity(quantity);

        return mapToInventoryResponse(
                productRepository.save(product));
    }

    private InventoryResponse mapToInventoryResponse(Product product) {

        boolean lowStock = product.getStockQuantity() <= 10;

        return new InventoryResponse(
                product.getId(),
                product.getName(),
                product.getStockQuantity(),
                lowStock
        );
    }
}