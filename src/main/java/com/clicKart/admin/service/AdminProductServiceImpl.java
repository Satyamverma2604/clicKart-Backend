package com.clicKart.admin.service;

import org.springframework.stereotype.Service;

import com.clicKart.product.dto.ProductRequest;
import com.clicKart.product.dto.ProductResponse;
import com.clicKart.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductService productService;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        return productService.createProduct(request);
    }
}