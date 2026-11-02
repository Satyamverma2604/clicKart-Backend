package com.clicKart.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clicKart.category.dto.CategoryRequest;
import com.clicKart.category.dto.CategoryResponse;
import com.clicKart.category.entity.Category;
import com.clicKart.category.repository.CategoryRepository;
import com.clicKart.exception.DuplicateResourceException;
import com.clicKart.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

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

        return response;
    }
    
   
	private Category getCategoryByIdOrThrow(Long id) {
		// TODO Check Category exists or not
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not found with  id " + id));

	}
}