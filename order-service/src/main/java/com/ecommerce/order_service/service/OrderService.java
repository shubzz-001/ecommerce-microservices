package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.dto.ProductResponse;
import com.ecommerce.order_service.event.OrderCancelledEvent;
import com.ecommerce.order_service.exception.AccessDeniedException;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderStatus;
import com.ecommerce.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String userEmail) {

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
                .userEmail(userEmail)
                .createdAt(LocalDateTime.now())
                .build();
        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                saved.getStatus(),
                saved.getUserEmail(),
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
                    order.getUserEmail(),
                    order.getCreatedAt()
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    public OrderResponse getOrderById(Long id, String email, String role) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }

        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getUserEmail(),
                order.getCreatedAt()
        );
    }

    public OrderResponse cancelOrder(Long id, String email, String role) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Order already cancelled");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order saved = orderRepository.save(order);

        kafkaTemplate.send(
                "order-cancelled-topic",
                new OrderCancelledEvent(
                        saved.getProductId(),
                        saved.getQuantity()
                )
        );

        return new OrderResponse(
                saved.getId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                saved.getStatus(),
                saved.getUserEmail(),
                saved.getCreatedAt()
        );
    }

    public List<OrderResponse> getOrdersByUser(String userEmail) {
        List<Order> orders = orderRepository.findByUserEmail(userEmail);
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse(
                    order.getId(),
                    order.getProductId(),
                    order.getQuantity(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getUserEmail(),
                    order.getCreatedAt()
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    public String deleteOrder(Long id, String email,  String role) {
        Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }

        orderRepository.deleteById(id);
        return "Product Deleted Successfully";
    }
}
