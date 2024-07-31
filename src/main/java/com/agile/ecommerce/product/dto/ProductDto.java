package com.agile.ecommerce.product.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public final class ProductDto {
    @Nullable
    public Long id;
    public @NotBlank(message = "Name cannot be blank") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name;
    public @NotBlank(message = "Description cannot be blank") @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description;
    public @NotNull(message = "Price cannot be null") @Min(value = 0, message = "Price must be non-negative") BigDecimal price;
    public @Min(value = 0, message = "Quantity must be non-negative") int quantity;

    public ProductDto(
            @Nullable
            Long id,
            @NotBlank(message = "Name cannot be blank")
            @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
            String name,
            @NotBlank(message = "Description cannot be blank")
            @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
            String description,
            @NotNull(message = "Price cannot be null")
            @Min(value = 0, message = "Price must be non-negative")
            BigDecimal price,
            @Min(value = 0, message = "Quantity must be non-negative")
            int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    @Nullable
    public Long id() {
        return id;
    }

    public @NotBlank(message = "Name cannot be blank") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name() {
        return name;
    }

    public @NotBlank(message = "Description cannot be blank") @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description() {
        return description;
    }

    public @NotNull(message = "Price cannot be null") @Min(value = 0, message = "Price must be non-negative") BigDecimal price() {
        return price;
    }

    public @Min(value = 0, message = "Quantity must be non-negative") int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProductDto) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.price, that.price) &&
                this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, quantity);
    }

    public ProductDto() {
    }

    @Override
    public String toString() {
        return "ProductDto[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "price=" + price + ", " +
                "quantity=" + quantity + ']';
    }

}
