package com.clicKart.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clicKart.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByActiveTrue();

	List<Product> findByCategoryId(Long categoryId);

	List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

	List<Product> findByBrandIgnoreCase(String brand);

	List<Product> findByNameContainingIgnoreCase(String name);

	List<Product> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name);

	boolean existsByNameIgnoreCase(String name);

	boolean existsByNameIgnoreCaseAndBrandIgnoreCase(String name, String brand);

	boolean existsByNameIgnoreCaseAndBrandIgnoreCaseAndIdNot(String name, String brand, Long id);

	Page<Product> findByActiveTrue(Pageable pageable);
	
	List<Product> findByStockQuantityLessThanEqual(Integer quantity);
}

/*
 * Upcoming methods ---> price range brand category keyword stock sorting
 * pagination
 */