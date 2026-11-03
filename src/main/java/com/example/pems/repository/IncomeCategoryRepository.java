package com.example.pems.repository;

import com.example.pems.entity.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    List<IncomeCategory> findByActiveTrue();
}
