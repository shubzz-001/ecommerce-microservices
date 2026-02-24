package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.dto.ProductResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderResponse createOrder(OrderRequest request) {

        ProductResponse product = productClient.getProduct(request.productId());

        if (product.stock() < request.quantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        productClient.reduceStock(request.productId(), request.quantity());

        BigDecimal total = product.price()
                .multiply(BigDecimal.valueOf(request.quantity()));

        Order order = Order.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .totalPrice(total)
                .status("CREATED")
                .build();

        Order saved = orderRepository.save(order);
        return new OrderResponse(
                saved.getId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                saved.getStatus()
        );
    }

}
