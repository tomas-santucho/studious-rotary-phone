package com.agile.ecommerce.order.core;

import com.agile.ecommerce.order.data.OrderRepository;
import com.agile.ecommerce.order.domain.Order;
import com.agile.ecommerce.order.dto.OrderDto;
import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.product.data.ProductRepository;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        modelMapper = mock(ModelMapper.class);
        orderService = new OrderService(orderRepository, productRepository, modelMapper);
    }

    @Nested
    @DisplayName("getAll Tests")
    class GetAllTests {

        @Test
        @DisplayName("should return paginated list of orders")
        void shouldReturnPaginatedListOfOrders() {
            var pageable = PageRequest.of(0, 10);
            Order order = new Order();
            order.setId(1L);
            Page<Order> page = new PageImpl<>(List.of(order), pageable, 1);

            when(orderRepository.findAll(pageable)).thenReturn(page);
            when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenAnswer(invocation -> {
                Order o = invocation.getArgument(0);
                return new OrderDto(o.getId(), o.getOrderDate(), o.getCustomerName(), o.getCustomerAddress(), List.of());
            });

            Page<OrderDto> result = orderService.getAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(orderRepository, times(1)).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("getById Tests")
    class GetByIdTests {

        @Test
        @DisplayName("should return order when found")
        void shouldReturnOrderWhenFound() throws OrderNotFoundException {
            Order order = new Order();
            order.setId(1L);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
            when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenAnswer(invocation -> {
                Order o = invocation.getArgument(0);
                return new OrderDto(o.getId(), o.getOrderDate(), o.getCustomerName(), o.getCustomerAddress(), List.of());
            });

            OrderDto result = orderService.getById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            verify(orderRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when order not found")
        void shouldThrowExceptionWhenOrderNotFound() {
            when(orderRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(OrderNotFoundException.class, () -> orderService.getById(1L));

            verify(orderRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("add Tests")
    class AddTests {

        @Test
        @DisplayName("should add and return order")
        void shouldAddAndReturnOrder() throws ProductNotFoundException {
            OrderDto orderDto = new OrderDto(1L, LocalDateTime.now(), "CustomerName", "CustomerAddress", List.of());
            Order order = new Order();
            order.setId(1L);
            order.setCustomerName("CustomerName");

            when(modelMapper.map(orderDto, Order.class)).thenReturn(order);
            when(orderRepository.save(any(Order.class))).thenReturn(order);
            when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

            OrderDto result = orderService.add(orderDto);

            assertNotNull(result);
            assertEquals(orderDto.customerName(), result.customerName());
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            OrderItemDto orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);
            OrderDto orderDto = new OrderDto(1L, LocalDateTime.now(), "CustomerName", "CustomerAddress", List.of(orderItemDto));

            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> orderService.add(orderDto));

            verify(productRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update and return order")
        void shouldUpdateAndReturnOrder() throws OrderNotFoundException, ProductNotFoundException {
            OrderDto orderDto = new OrderDto(1L, LocalDateTime.now(), "CustomerName", "CustomerAddress", List.of());
            Order order = new Order();
            order.setId(1L);
            order.setCustomerName("CustomerName");

            when(orderRepository.existsById(1L)).thenReturn(true);
            when(modelMapper.map(orderDto, Order.class)).thenReturn(order);
            when(orderRepository.save(any(Order.class))).thenReturn(order);
            when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

            OrderDto result = orderService.update(orderDto);

            assertNotNull(result);
            assertEquals(orderDto.customerName(), result.customerName());
            verify(orderRepository, times(1)).existsById(1L);
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("should throw exception when order not found")
        void shouldThrowExceptionWhenOrderNotFound() {
            OrderDto orderDto = new OrderDto(1L, LocalDateTime.now(), "CustomerName", "CustomerAddress", List.of());

            when(orderRepository.existsById(orderDto.id())).thenReturn(false);

            assertThrows(OrderNotFoundException.class, () -> orderService.update(orderDto));

            verify(orderRepository, times(1)).existsById(orderDto.id());
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete order by id")
        void shouldDeleteOrderById() throws OrderNotFoundException {
            when(orderRepository.existsById(1L)).thenReturn(true);

            orderService.delete(1L);

            verify(orderRepository, times(1)).existsById(1L);
            verify(orderRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when order not found")
        void shouldThrowExceptionWhenOrderNotFound() {
            when(orderRepository.existsById(1L)).thenReturn(false);

            assertThrows(OrderNotFoundException.class, () -> orderService.delete(1L));

            verify(orderRepository, times(1)).existsById(1L);
        }
    }
}
