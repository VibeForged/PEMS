package com.example.pems.dto;

import com.example.pems.entity.User;
import java.time.LocalDateTime;

public record AdminUserResponse(Long id, String name, String email, User.Role role,
                                boolean active, LocalDateTime createdAt, long transactionCount) {
}
