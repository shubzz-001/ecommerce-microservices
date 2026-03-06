package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.dto.ProductResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderStatus;
import com.ecommerce.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Transactional
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
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse(
                    order.getId(),
                    order.getProductId(),
                    order.getQuantity(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getCreatedAt()
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    public OrderResponse getOrderById(@Valid Long id) {
        
    }

    public List<OrderResponse> getOrdersByUserId(String name) {
        
    }

    public OrderResponse cancelOrder(@Valid Long id) {
    }
}
