package com.ecommerce.order_service.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal totalPrice,
        String status
) {
}
