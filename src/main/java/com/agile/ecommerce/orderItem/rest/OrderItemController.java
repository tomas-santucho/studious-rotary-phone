package com.agile.ecommerce.orderItem.rest;

import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.core.OrderItemService;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.orderItem.exception.OrderItemNotFoundException;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/order-items")
@Tag(name = "OrderItems", description = "API for managing order items")
public final class OrderItemController {
    private final OrderItemService service;

    @Operation(summary = "Get all order items", description = "Retrieve a paginated list of all order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order items"),
            @ApiResponse(responseCode = "404", description = "Order items not found")
    })
    @GetMapping
    public ResponseEntity<Page<OrderItemDto>> getAllOrderItems(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @Operation(summary = "Get order item by ID", description = "Retrieve a specific order item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order item"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable long id) throws OrderItemNotFoundException {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Add a new order item", description = "Create a new order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created")
    })
    @PostMapping
    public ResponseEntity<OrderItemDto> addOrderItem(@RequestBody OrderItemDto orderItemDto) throws ProductNotFoundException, OrderNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(orderItemDto));
    }

    @Operation(summary = "Update an order item", description = "Update an existing order item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @PutMapping
    public ResponseEntity<OrderItemDto> updateOrderItem(@RequestBody OrderItemDto orderItemDto) throws OrderItemNotFoundException, ProductNotFoundException {
        return ResponseEntity.ok(service.update(orderItemDto));
    }

    @Operation(summary = "Delete an order item", description = "Delete a specific order item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
