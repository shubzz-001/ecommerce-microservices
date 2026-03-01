package com.ecommerce.user_service.dto;

import com.ecommerce.user_service.model.Role;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
