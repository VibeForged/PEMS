package com.example.pems.service;

import com.example.pems.dto.AdminDashboardResponse;
import com.example.pems.dto.AdminUserResponse;
import com.example.pems.entity.User;
import com.example.pems.exception.BadRequestException;
import com.example.pems.exception.ResourceNotFoundException;
import com.example.pems.repository.ExpenseRepository;
import com.example.pems.repository.IncomeRepository;
import com.example.pems.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public AdminService(UserRepository userRepository, ExpenseRepository expenseRepository,
                        IncomeRepository incomeRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public Page<AdminUserResponse> getUsers(String search, Pageable pageable) {
        String query = search == null ? "" : search.trim();
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        query, query, pageable)
                .map(this::toResponse);
    }

    public AdminUserResponse setAccountActive(Long userId, boolean active, String actorEmail) {
        User user = getUser(userId);
        ensureNotSelf(user, actorEmail);
        user.setActive(active);
        return toResponse(userRepository.save(user));
    }

    public void deleteAccount(Long userId, String actorEmail) {
        User user = getUser(userId);
        ensureNotSelf(user, actorEmail);
        userRepository.delete(user);
    }

    public AdminDashboardResponse getDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDateTime monthStartTime = monthStart.atStartOfDay();
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActive(true);
        long expenseCount = expenseRepository.count();
        long incomeCount = incomeRepository.count();

        return new AdminDashboardResponse(
                totalUsers,
                activeUsers,
                userRepository.countByActive(false),
                userRepository.countByCreatedAtAfter(monthStartTime),
                expenseCount + incomeCount,
                incomeRepository.getTotalAmount(),
                expenseRepository.getTotalAmount(),
                incomeRepository.getTotalAmountBetween(monthStart, today),
                expenseRepository.getTotalAmountBetween(monthStart, today));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void ensureNotSelf(User user, String actorEmail) {
        if (user.getEmail().equalsIgnoreCase(actorEmail)) {
            throw new BadRequestException("You cannot modify your own administrator account");
        }
    }

    private AdminUserResponse toResponse(User user) {
        long transactionCount = expenseRepository.countByUserId(user.getId())
                + incomeRepository.countByUserId(user.getId());
        return new AdminUserResponse(user.getId(), user.getName(), user.getEmail(),
                user.getRole(), user.isActive(), user.getCreatedAt(), transactionCount);
    }
}
