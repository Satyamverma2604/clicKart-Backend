package com.clicKart.product.service;



import java.util.List;

import com.clicKart.product.dto.ProductRequest;
import com.clicKart.product.dto.ProductResponse;



public interface ProductService {
	
	

    ProductResponse	 createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getActiveProducts();

    List<ProductResponse> getProductsByCategory(Long categoryId);

    List<ProductResponse> searchProducts(String name);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
    
    
    
    

    List<ProductResponse> getLowStockProducts();

    ProductResponse updateStock(Long productId, Integer quantity);
    
    ProductResponse updateProductStatus(Long id, Boolean active);
}