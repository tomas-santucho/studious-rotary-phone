package com.agile.ecommerce.order.rest;

import com.agile.ecommerce.order.core.OrderService;
import com.agile.ecommerce.order.dto.OrderDto;
import com.agile.ecommerce.order.exception.OrderNotFoundException;
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
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "API for managing orders")
public final class OrderController {
    private final OrderService service;

    @Operation(summary = "Get all orders", description = "Retrieve a paginated list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the orders"),
            @ApiResponse(responseCode = "404", description = "Orders not found")
    })
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable long id) throws OrderNotFoundException {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Add a new order", description = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created")
    })
    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) throws ProductNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(orderDto));
    }

    @Operation(summary = "Update an order", description = "Update an existing order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) throws OrderNotFoundException, ProductNotFoundException {
        return ResponseEntity.ok(service.update(orderDto));
    }

    @Operation(summary = "Delete an order", description = "Delete a specific order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) throws OrderNotFoundException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
