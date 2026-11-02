package com.clicKart.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.category.entity.Category;
import com.clicKart.category.repository.CategoryRepository;
import com.clicKart.exception.BadRequestException;
import com.clicKart.exception.DuplicateResourceException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.product.dto.ProductRequest;
import com.clicKart.product.dto.ProductResponse;
import com.clicKart.product.entity.Product;
import com.clicKart.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

	private final ProductRepository productRepository;

	private final CategoryRepository categoryRepository;

	@Override
	public ProductResponse createProduct(ProductRequest request) {

		// TODO Check duplicate Product
		if (productRepository.existsByNameIgnoreCaseAndBrandIgnoreCase(request.getName(), request.getBrand())) {
			throw new DuplicateResourceException(
					"Product " + request.getName() + " already exists with brand " + request.getBrand());
		}

		// TODO Check Category exists or not

		Category category = getCategoryByIdOrThrow(request.getCategoryId());

		// TODO DTO to Entity
		Product product = new Product();
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setStockQuantity(request.getStockQuantity());
		product.setBrand(request.getBrand());
		product.setImageUrl(request.getImageUrl());
		product.setActive(request.getActive());
		product.setCategory(category);

		product = productRepository.save(product);

		// TODO Entity to DTO Response
		return mapToResponse(product);
	}

	// TODO Fetching Product by its id
	@Override
	public ProductResponse getProductById(Long id) {

		Product product = getProductByIdOrThrow(id);

		return mapToResponse(product);
	}

	@Override
	public List<ProductResponse> getAllProducts() {
		List<ProductResponse> response = productRepository.findAll().stream().map(this::mapToResponse).toList();
		return response;
	}

	@Override
	public List<ProductResponse> getActiveProducts() {
		List<ProductResponse> response = productRepository.findByActiveTrue().stream().map(this::mapToResponse)
				.toList();
		return response;
	}

	@Override
	public List<ProductResponse> getProductsByCategory(Long categoryId) {
		// TODO Auto-generated method stub
		this.getCategoryByIdOrThrow(categoryId);
		List<ProductResponse> response = productRepository.findByCategoryIdAndActiveTrue(categoryId).stream()
				.map(this::mapToResponse).toList();
		return response;
	}

	@Override
	public List<ProductResponse> searchProducts(String name) {
		// TODO Auto-generated method stub
		List<ProductResponse> response = productRepository.findByNameContainingIgnoreCase(name).stream()
				.map(this::mapToResponse).toList();
		return response;
	}

	// TODO Update Product
	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {

		// TODO Find product by product id
		Product product = getProductByIdOrThrow(id);
		// TODO Find Category by request Category id

		Category category = getCategoryByIdOrThrow(request.getCategoryId());

		// TODO Checking duplicates in DB
		if (productRepository.existsByNameIgnoreCaseAndBrandIgnoreCaseAndIdNot(request.getName(), request.getBrand(),
				product.getId())) {

			throw new DuplicateResourceException("Another product already exists with name: " + request.getName() + " and brand: "
					+ request.getBrand());
		}

		// TODO Updating Product instance
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setStockQuantity(request.getStockQuantity());
		product.setBrand(request.getBrand());
		product.setImageUrl(request.getImageUrl());
		product.setCategory(category);

		// TODO Updating product in DB

		product = productRepository.save(product);

		return mapToResponse(product);
	}

	@Override
	public void deleteProduct(Long id) {
		// TODO Find product by product id
		Product product = getProductByIdOrThrow(id);
		// TODO Deleting in Database
		product.setActive(false);
		productRepository.save(product);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getLowStockProducts() {

		
	    List<Product> products =
	            productRepository.findByStockQuantityLessThanEqual(10);

	    return products.stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	
	@Override
	@Transactional
	public ProductResponse updateStock(
	        Long productId,
	        Integer quantity) {

	    Product product = getProductByIdOrThrow(productId);

	    if (quantity == null || quantity < 0) {
	        throw new BadRequestException(
	                "Stock quantity cannot be negative");
	    }

	    product.setStockQuantity(quantity);

	    Product updatedProduct =
	            productRepository.save(product);

	    return mapToResponse(updatedProduct);
	}
	
	
	@Override
	@Transactional
	public ProductResponse updateProductStatus(Long id, Boolean active) {

	    Product product = productRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Product not found with id: " + id));

	    if (active == null) {
	        throw new BadRequestException(
	                "Product active status is required");
	    }

	    product.setActive(active);

	    Product updatedProduct = productRepository.save(product);

	    return mapToResponse(updatedProduct);
	}
	
	/*******************************************
	 *             HELPER METHODS              *
	 *******************************************/
	
	
	

	// TODO Entity to DTO Response
	private ProductResponse mapToResponse(Product product) {

		ProductResponse response = new ProductResponse();

		response.setId(product.getId());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setPrice(product.getPrice());
		response.setStockQuantity(product.getStockQuantity());
		response.setBrand(product.getBrand());
		response.setImageUrl(product.getImageUrl());
		response.setActive(product.getActive());
		response.setCategoryId(product.getCategory().getId());
		response.setCategoryName(product.getCategory().getName());

		return response;
	}

	
	private Category getCategoryByIdOrThrow(Long id) {
		// TODO Check Category exists or not
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not found with  id " + id));

	}

	private Product getProductByIdOrThrow(Long id) {
		// TODO Check Category exists or not
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

}
