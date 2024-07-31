package com.agile.ecommerce.order.rest;

import com.agile.ecommerce.order.core.OrderService;
import com.agile.ecommerce.order.domain.Order;
import com.agile.ecommerce.order.dto.OrderDto;
import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@DisplayName("OrderController Tests")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName("CustomerName");
        order.setCustomerAddress("CustomerAddress");

        orderDto = new OrderDto(1L, LocalDateTime.now(), "CustomerName", "CustomerAddress", List.of());
    }

    @Nested
    @DisplayName("getAllOrders Tests")
    class GetAllOrdersTests {

        @Test
        @DisplayName("should return paginated list of orders")
        void shouldReturnPaginatedListOfOrders() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            var page = new PageImpl<>(List.of(orderDto), pageable, 1);
            when(service.getAll(pageable)).thenReturn(page);

            mockMvc.perform(get("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(orderDto.id()));

            verify(service, times(1)).getAll(pageable);
        }
    }

    @Nested
    @DisplayName("getOrderById Tests")
    class GetOrderByIdTests {

        @Test
        @DisplayName("should return order when found")
        void shouldReturnOrderWhenFound() throws Exception {
            when(service.getById(1L)).thenReturn(orderDto);

            mockMvc.perform(get("/api/orders/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(orderDto.id()));

            verify(service, times(1)).getById(1L);
        }

        @Test
        @DisplayName("should return 404 when order not found")
        void shouldReturn404WhenOrderNotFound() throws Exception {
            when(service.getById(1L)).thenThrow(new OrderNotFoundException(1L));

            mockMvc.perform(get("/api/orders/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).getById(1L);
        }
    }

    @Nested
    @DisplayName("addOrder Tests")
    class AddOrderTests {

        @Test
        @DisplayName("should create and return order")
        void shouldCreateAndReturnOrder() throws Exception {
            when(service.add(any(OrderDto.class))).thenReturn(orderDto);

            mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(orderDto.id()));

            verify(service, times(1)).add(any(OrderDto.class));
        }
    }

    @Nested
    @DisplayName("updateOrder Tests")
    class UpdateOrderTests {

        @Test
        @DisplayName("should update and return order")
        void shouldUpdateAndReturnOrder() throws Exception {
            when(service.update(any(OrderDto.class))).thenReturn(orderDto);

            mockMvc.perform(put("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(orderDto.id()));

            verify(service, times(1)).update(any(OrderDto.class));
        }

        @Test
        @DisplayName("should return 404 when order not found")
        void shouldReturn404WhenOrderNotFound() throws Exception {
            when(service.update(any(OrderDto.class))).thenThrow(new OrderNotFoundException(1L));

            mockMvc.perform(put("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderDto)))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).update(any(OrderDto.class));
        }
    }

    @Nested
    @DisplayName("deleteOrder Tests")
    class DeleteOrderTests {

        @Test
        @DisplayName("should delete order by id")
        void shouldDeleteOrderById() throws Exception {
            mockMvc.perform(delete("/api/orders/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(service, times(1)).delete(1L);
        }

        @Test
        @DisplayName("should return 404 when order not found")
        void shouldReturn404WhenOrderNotFound() throws Exception {
            doThrow(new OrderNotFoundException(1L)).when(service).delete(1L);

            mockMvc.perform(delete("/api/orders/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).delete(1L);
        }
    }
}
