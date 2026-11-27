
package com.clicKart.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clicKart.category.dto.CategoryRequest;
import com.clicKart.category.dto.CategoryResponse;
import com.clicKart.category.entity.Category;
import com.clicKart.category.repository.CategoryRepository;
import com.clicKart.exception.DuplicateResourceException;
import com.clicKart.exception.InvalidOperationException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category already exists");
        }

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {

        Category category = getCategoryByIdOrThrow(id);

        return mapToResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = getCategoryByIdOrThrow(id);

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return mapToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = getCategoryByIdOrThrow(id);

        long productCount = productRepository.countByCategoryId(id);

        if (productCount > 0) {
            throw new InvalidOperationException(
                "Cannot delete category because " + productCount +
                " product(s) are assigned to it. " +
                "Please reassign or deactivate the products first."
            );
        }

        categoryRepository.delete(category);
    }

    /*******************************************
     *             HELPER METHODS              *
     *******************************************/

    private CategoryResponse mapToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setActive(category.getActive());

        // Count products belonging to this category
        response.setProductCount(
                productRepository.countByCategoryId(category.getId())
        );

        return response;
    }

    private Category getCategoryByIdOrThrow(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category Not found with id " + id
                        )
                );
    }
}
