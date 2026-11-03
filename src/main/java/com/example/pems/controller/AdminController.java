package com.example.pems.controller;

import com.example.pems.dto.*;
import com.example.pems.entity.Category;
import com.example.pems.entity.IncomeCategory;
import com.example.pems.service.AdminService;
import com.example.pems.service.CategoryService;
import com.example.pems.service.IncomeCategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final CategoryService categoryService;
    private final IncomeCategoryService incomeCategoryService;

    public AdminController(AdminService adminService, CategoryService categoryService,
                           IncomeCategoryService incomeCategoryService) {
        this.adminService = adminService;
        this.categoryService = categoryService;
        this.incomeCategoryService = incomeCategoryService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {
        return adminService.getDashboard();
    }

    @GetMapping("/users")
    public Page<AdminUserResponse> users(@RequestParam(required = false) String search,
                                         Pageable pageable) {
        return adminService.getUsers(search, pageable);
    }

    @PatchMapping("/users/{id}/status")
    public AdminUserResponse setUserStatus(@PathVariable Long id,
                                           @Valid @RequestBody AccountStateRequest request,
                                           Authentication authentication) {
        return adminService.setAccountActive(id, request.active(), authentication.getName());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @Valid @RequestBody DeleteAccountRequest request,
                                           Authentication authentication) {
        adminService.deleteAccount(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories/expenses")
    public List<Category> expenseCategories() {
        return categoryService.getAllCategoriesForAdmin();
    }

    @PostMapping("/categories/expenses")
    public Category createExpenseCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return categoryService.createCategory(category);
    }

    @PutMapping("/categories/expenses/{id}")
    public ResponseEntity<Category> updateExpenseCategory(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return categoryService.updateCategory(id, category)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/categories/expenses/{id}/status")
    public ResponseEntity<Void> setExpenseCategoryStatus(@PathVariable Long id,
                                                          @Valid @RequestBody AccountStateRequest request) {
        return categoryService.setCategoryActive(id, request.active())
                ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/categories/incomes")
    public List<IncomeCategory> incomeCategories() {
        return incomeCategoryService.getAllCategoriesForAdmin();
    }

    @PostMapping("/categories/incomes")
    public IncomeCategory createIncomeCategory(@Valid @RequestBody CategoryRequest request) {
        IncomeCategory category = new IncomeCategory();
        category.setName(request.name());
        return incomeCategoryService.createCategory(category);
    }

    @PutMapping("/categories/incomes/{id}")
    public ResponseEntity<IncomeCategory> updateIncomeCategory(@PathVariable Long id,
                                                                @Valid @RequestBody CategoryRequest request) {
        IncomeCategory category = new IncomeCategory();
        category.setName(request.name());
        return incomeCategoryService.updateCategory(id, category)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/categories/incomes/{id}/status")
    public ResponseEntity<Void> setIncomeCategoryStatus(@PathVariable Long id,
                                                         @Valid @RequestBody AccountStateRequest request) {
        return incomeCategoryService.setCategoryActive(id, request.active())
                ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
