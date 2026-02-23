package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull
        @Positive(message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull
        @Min(value = 0, message = "Stock can not be negative")
        Integer stock
) {}
