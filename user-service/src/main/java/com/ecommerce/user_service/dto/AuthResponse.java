package com.ecommerce.user_service.dto;

public record AuthResponse(
        String email,
        String token
) {
}
