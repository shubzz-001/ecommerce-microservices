package com.ecommerce.product_service.event;

public record OrderCancelledEvent(
        Long productId,
        int quantity
) {
}
