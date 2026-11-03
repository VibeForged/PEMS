package com.example.pems.repository;

import com.example.pems.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    long countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    BigDecimal getTotalAmount();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :from AND :to")
    BigDecimal getTotalAmountBetween(LocalDate from, LocalDate to);

    Page<Expense> findByUserId(Long userId, Pageable pageable);

    Page<Expense> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId,
            Pageable pageable
    );

    Page<Expense> findByUserIdAndExpenseDateBetween(
            Long userId,
            java.time.LocalDate from,
            java.time.LocalDate to,
            Pageable pageable
    );

    Page<Expense> findByUserIdAndCategoryIdAndExpenseDateBetween(
            Long userId,
            Long categoryId,
            java.time.LocalDate from,
            java.time.LocalDate to,
            Pageable pageable
    );
}
