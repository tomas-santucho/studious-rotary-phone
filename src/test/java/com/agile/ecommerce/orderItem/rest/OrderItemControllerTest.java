package com.agile.ecommerce.orderItem.rest;

import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.core.OrderItemService;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.orderItem.exception.OrderItemNotFoundException;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderItemController.class)
@DisplayName("OrderItemController Tests")
class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemService service;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderItemDto orderItemDto;

    @BeforeEach
    void setUp() {
        orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);
    }

    @Nested
    @DisplayName("getAllOrderItems Tests")
    class GetAllOrderItemsTests {

        @Test
        @DisplayName("should return paginated list of order items")
        void shouldReturnPaginatedListOfOrderItems() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<OrderItemDto> page = new PageImpl<>(List.of(orderItemDto), pageable, 1);

            when(service.getAll(pageable)).thenReturn(page);

            mockMvc.perform(get("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].productId").value(orderItemDto.productId));

            verify(service, times(1)).getAll(pageable);
        }
    }

    @Nested
    @DisplayName("getOrderItemById Tests")
    class GetOrderItemByIdTests {

        @Test
        @DisplayName("should return order item when found")
        void shouldReturnOrderItemWhenFound() throws Exception {
            when(service.getById(1L)).thenReturn(orderItemDto);

            mockMvc.perform(get("/api/order-items/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(orderItemDto.productId));

            verify(service, times(1)).getById(1L);
        }

        @Test
        @DisplayName("should return 404 when order item not found")
        void shouldReturn404WhenOrderItemNotFound() throws Exception {
            when(service.getById(1L)).thenThrow(new OrderItemNotFoundException(1L));

            mockMvc.perform(get("/api/order-items/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).getById(1L);
        }
    }

    @Nested
    @DisplayName("addOrderItem Tests")
    class AddOrderItemTests {

        @Test
        @DisplayName("should create and return order item")
        void shouldCreateAndReturnOrderItem() throws Exception {
            when(service.add(any(OrderItemDto.class))).thenReturn(orderItemDto);

            mockMvc.perform(post("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderItemDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.productId").value(orderItemDto.productId));

            verify(service, times(1)).add(any(OrderItemDto.class));
        }

        @Test
        @DisplayName("should return 404 when order not found")
        void shouldReturn404WhenOrderNotFound() throws Exception {
            when(service.add(any(OrderItemDto.class))).thenThrow(new OrderNotFoundException(1L));

            mockMvc.perform(post("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderItemDto)))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).add(any(OrderItemDto.class));
        }

        @Test
        @DisplayName("should return 404 when product not found")
        void shouldReturn404WhenProductNotFound() throws Exception {
            when(service.add(any(OrderItemDto.class))).thenThrow(new ProductNotFoundException(1L));

            mockMvc.perform(post("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderItemDto)))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).add(any(OrderItemDto.class));
        }
    }

    @Nested
    @DisplayName("updateOrderItem Tests")
    class UpdateOrderItemTests {

        @Test
        @DisplayName("should update and return order item")
        void shouldUpdateAndReturnOrderItem() throws Exception {
            when(service.update(any(OrderItemDto.class))).thenReturn(orderItemDto);

            mockMvc.perform(put("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderItemDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(orderItemDto.productId));

            verify(service, times(1)).update(any(OrderItemDto.class));
        }

        @Test
        @DisplayName("should return 404 when order item not found")
        void shouldReturn404WhenOrderItemNotFound() throws Exception {
            when(service.update(any(OrderItemDto.class))).thenThrow(new OrderItemNotFoundException(1L));

            mockMvc.perform(put("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderItemDto)))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).update(any(OrderItemDto.class));
        }

        @Test
        @DisplayName("should return 404 when product not found")
        void shouldReturn404WhenProductNotFound() throws Exception {
            when(service.update(any(OrderItemDto.class))).thenThrow(new ProductNotFoundException(1L));

            mockMvc.perform(put("/api/order-items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderItemDto)))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).update(any(OrderItemDto.class));
        }
    }

    @Nested
    @DisplayName("deleteOrderItem Tests")
    class DeleteOrderItemTests {

        @Test
        @DisplayName("should delete order item by id")
        void shouldDeleteOrderItemById() throws Exception {
            mockMvc.perform(delete("/api/order-items/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(service, times(1)).delete(1L);
        }
    }
}
