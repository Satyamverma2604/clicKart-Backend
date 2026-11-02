package com.clicKart.admin.service;

import com.clicKart.product.dto.ProductRequest;
import com.clicKart.product.dto.ProductResponse;

public interface AdminProductService {

    ProductResponse createProduct(ProductRequest request);
}