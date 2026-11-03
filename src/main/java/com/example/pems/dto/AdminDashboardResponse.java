package com.example.pems.dto;

import java.math.BigDecimal;

public record AdminDashboardResponse(long totalUsers, long activeUsers, long blockedUsers,
                                     long newUsersThisMonth, long totalTransactions,
                                     BigDecimal totalIncome, BigDecimal totalExpense,
                                     BigDecimal monthlyIncome, BigDecimal monthlyExpense) {
}
