package com.example.pems.service;

import com.example.pems.entity.IncomeCategory;
import com.example.pems.repository.IncomeCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;

    public IncomeCategoryService(IncomeCategoryRepository incomeCategoryRepository) {
        this.incomeCategoryRepository = incomeCategoryRepository;
    }

    public List<IncomeCategory> getAllCategories() {
        return incomeCategoryRepository.findByActiveTrue();
    }

    public List<IncomeCategory> getAllCategoriesForAdmin() {
        return incomeCategoryRepository.findAll();
    }

    public Optional<IncomeCategory> getCategoryById(Long id) {
        return incomeCategoryRepository.findById(id);
    }

    public IncomeCategory createCategory(IncomeCategory category) {
        return incomeCategoryRepository.save(category);
    }

    public Optional<IncomeCategory> updateCategory(
            Long id,
            IncomeCategory updatedCategory) {

        return incomeCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategory.getName());
                    return incomeCategoryRepository.save(existingCategory);
                });
    }

    public boolean deleteCategory(Long id) {
        return setCategoryActive(id, false);
    }

    public boolean setCategoryActive(Long id, boolean active) {
        return incomeCategoryRepository.findById(id)
                .map(category -> {
                    category.setActive(active);
                    incomeCategoryRepository.save(category);
                    return true;
                })
                .orElse(false);
    }
}
