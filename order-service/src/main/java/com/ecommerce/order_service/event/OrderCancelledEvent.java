package com.ecommerce.order_service.event;

public record OrderCancelledEvent(
        Long productId,
        int quantity
) {
}
