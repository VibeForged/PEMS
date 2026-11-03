package com.example.pems.repository;

import com.example.pems.entity.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    long countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i")
    BigDecimal getTotalAmount();

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.incomeDate BETWEEN :from AND :to")
    BigDecimal getTotalAmountBetween(LocalDate from, LocalDate to);

    Page<Income> findByUserId(Long userId, Pageable pageable);

    Page<Income> findByUserIdAndIncomeCategoryId(
            Long userId,
            Long categoryId,
            Pageable pageable
    );

    Page<Income> findByUserIdAndIncomeDateBetween(
            Long userId,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );

    Page<Income> findByUserIdAndIncomeCategoryIdAndIncomeDateBetween(
            Long userId,
            Long categoryId,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );
}
