package com.clicKart.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){
			ProductResponse productResponse = productService.createProduct(productRequest);
			ResponseEntity<ProductResponse> response = ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
		return  response;
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
		ProductResponse response = productService.getProductById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping()
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		List<ProductResponse> response = productService.getAllProducts();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/active")
	public ResponseEntity<List<ProductResponse>> getActiveProducts(){
		List<ProductResponse> response = productService.getActiveProducts();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId){
		List<ProductResponse> response = productService.getProductsByCategory(categoryId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name){
		List<ProductResponse> response = productService.searchProducts(name);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductRequest productRequest){
		ProductResponse response = productService.updateProduct(id, productRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		 productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
	
	
	

}
