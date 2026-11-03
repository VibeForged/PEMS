package com.example.pems.service;

import com.example.pems.entity.Category;
import com.example.pems.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findByActiveTrue();
    }

    public List<Category> getAllCategoriesForAdmin() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategory.getName());
                    return categoryRepository.save(existingCategory);
                });
    }

    public boolean deleteCategory(Long id) {
        return setCategoryActive(id, false);
    }

    public boolean setCategoryActive(Long id, boolean active) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setActive(active);
                    categoryRepository.save(category);
                    return true;
                })
                .orElse(false);
    }
}
