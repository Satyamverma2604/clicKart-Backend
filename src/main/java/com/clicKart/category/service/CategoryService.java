package com.clicKart.category.service;



import java.util.List;

import com.clicKart.category.dto.CategoryRequest;
import com.clicKart.category.dto.CategoryResponse;



public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}