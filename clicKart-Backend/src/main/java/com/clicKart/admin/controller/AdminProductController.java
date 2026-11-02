package com.clicKart.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clicKart.product.dto.ProductRequest;
import com.clicKart.product.dto.ProductResponse;
import com.clicKart.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

	private final ProductService productService;

	// Get All Products
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {

		List<ProductResponse> response = productService.getAllProducts();

		return ResponseEntity.ok(response);
	}

	// Get Product By ID
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {

		ProductResponse response = productService.getProductById(id);

		return ResponseEntity.ok(response);
	}

	// Create Product
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

		ProductResponse response = productService.createProduct(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Update Product
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {

		ProductResponse response = productService.updateProduct(id, request);

		return ResponseEntity.ok(response);
	}

	// Delete Product
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

		productService.deleteProduct(id);

		return ResponseEntity.noContent().build();
	}

	// Activate / Deactivate Product
	@PatchMapping("/{id}/status")
	public ResponseEntity<ProductResponse> updateProductStatus(@PathVariable Long id, @RequestParam Boolean active) {

		ProductResponse response = productService.updateProductStatus(id, active);

		return ResponseEntity.ok(response);
	}
}