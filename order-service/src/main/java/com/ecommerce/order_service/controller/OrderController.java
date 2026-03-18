package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestHeader("X-User-Email") String userEmail
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest, userEmail));
    }

    @GetMapping("/")   // Have to make it USer only and Admin only
    public ResponseEntity<List<?>> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @Valid @PathVariable Long id,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id, email, role));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(
            @Valid @PathVariable Long id,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(id, email, role));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(
            @RequestHeader("X-User-Email") String userEmail
    ) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userEmail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable Long id,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(orderService.deleteOrder(id, email, role));
    }
}
