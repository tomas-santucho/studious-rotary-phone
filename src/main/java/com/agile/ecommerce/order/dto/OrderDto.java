package com.agile.ecommerce.order.dto;

import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        @NotNull(message = "ID cannot be null")
        Long id,
        @NotNull(message = "Order date cannot be null")
        @PastOrPresent(message = "Order date cannot be in the future")
        LocalDateTime orderDate,
        @NotBlank(message = "Customer name cannot be blank")
        @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
        String customerName,
        @NotBlank(message = "Customer address cannot be blank")
        @Size(min = 5, max = 255, message = "Customer address must be between 5 and 255 characters")
        String customerAddress,
        @NotEmpty(message = "Order must contain at least one item")
        List<@Valid OrderItemDto> orderItems
) {}
