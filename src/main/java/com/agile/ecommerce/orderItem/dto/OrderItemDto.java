package com.agile.ecommerce.orderItem.dto;

import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
public final class OrderItemDto {
    public long productId;
    public @NotBlank(message = "Product name cannot be blank") @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters") String productName;
    public @Min(value = 1, message = "Quantity must be at least 1") int quantity;
    public @NotNull(message = "Price cannot be null") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") BigDecimal price;
    public long orderId;
    public OrderItemDto(
            long productId,
            @NotBlank(message = "Product name cannot be blank")
            @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
            String productName,
            @Min(value = 1, message = "Quantity must be at least 1")
            int quantity,
            @NotNull(message = "Price cannot be null")
            @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
            BigDecimal price,
            long orderId
    ) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OrderItemDto) obj;
        return this.productId == that.productId &&
                Objects.equals(this.productName, that.productName) &&
                this.quantity == that.quantity &&
                Objects.equals(this.price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, quantity, price);
    }

    @Override
    public String toString() {
        return "OrderItemDto[" +
                "productId=" + productId + ", " +
                "productName=" + productName + ", " +
                "quantity=" + quantity + ", " +
                "price=" + price + ']';
    }
}
