package com.example.pems.dto;

import com.example.pems.entity.User;
import java.time.LocalDateTime;

public record UserProfileResponse(Long id, String name, String email, User.Role role,
                                  boolean active, LocalDateTime createdAt) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(),
                user.getRole(), user.isActive(), user.getCreatedAt());
    }
}
