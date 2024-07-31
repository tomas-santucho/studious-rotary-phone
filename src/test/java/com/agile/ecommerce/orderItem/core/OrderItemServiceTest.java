package com.agile.ecommerce.orderItem.core;

import com.agile.ecommerce.order.data.OrderRepository;
import com.agile.ecommerce.order.domain.Order;
import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.data.OrderItemRepository;
import com.agile.ecommerce.orderItem.domain.OrderItem;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.orderItem.exception.OrderItemNotFoundException;
import com.agile.ecommerce.product.data.ProductRepository;
import com.agile.ecommerce.product.domain.Product;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderItemServiceTest {

    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        orderItemRepository = mock(OrderItemRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        modelMapper = mock(ModelMapper.class);
        orderItemService = new OrderItemService(orderItemRepository, productRepository, orderRepository, modelMapper);
    }

    @Nested
    @DisplayName("getAll Tests")
    class GetAllTests {

        @Test
        @DisplayName("should return paginated list of order items")
        void shouldReturnPaginatedListOfOrderItems() {
            Pageable pageable = PageRequest.of(0, 10);

            Product product = new Product();
            product.setId(1L);
            product.setName("ProductName");

            Order order = new Order();
            order.setId(1L);

            OrderItem orderItem = new OrderItem();
            orderItem.setId(1L);
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(2);
            orderItem.setPrice(BigDecimal.valueOf(100.0));

            Page<OrderItem> page = new PageImpl<>(List.of(orderItem), pageable, 1);

            when(orderItemRepository.findAll(pageable)).thenReturn(page);
            when(modelMapper.map(any(OrderItem.class), eq(OrderItemDto.class))).thenAnswer(invocation -> {
                OrderItem oi = invocation.getArgument(0);
                return new OrderItemDto(oi.getProduct().getId(), oi.getProduct().getName(), oi.getQuantity(), oi.getPrice(), oi.getOrder().getId());
            });

            Page<OrderItemDto> result = orderItemService.getAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(orderItemRepository, times(1)).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("getById Tests")
    class GetByIdTests {

        @Test
        @DisplayName("should return order item when found")
        void shouldReturnOrderItemWhenFound() throws OrderItemNotFoundException {
            Product product = new Product();
            product.setId(1L);
            product.setName("ProductName");

            Order order = new Order();
            order.setId(1L);

            OrderItem orderItem = new OrderItem();
            orderItem.setId(1L);
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(2);
            orderItem.setPrice(BigDecimal.valueOf(100.0));

            when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
            when(modelMapper.map(any(OrderItem.class), eq(OrderItemDto.class))).thenAnswer(invocation -> {
                OrderItem oi = invocation.getArgument(0);
                return new OrderItemDto(oi.getProduct().getId(), oi.getProduct().getName(), oi.getQuantity(), oi.getPrice(), oi.getOrder().getId());
            });

            OrderItemDto result = orderItemService.getById(1L);

            assertNotNull(result);
            assertEquals(1L, result.productId);
            verify(orderItemRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when order item not found")
        void shouldThrowExceptionWhenOrderItemNotFound() {
            when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(OrderItemNotFoundException.class, () -> orderItemService.getById(1L));

            verify(orderItemRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("add Tests")
    class AddTests {

        @Test
        @DisplayName("should add and return order item")
        void shouldAddAndReturnOrderItem() throws ProductNotFoundException, OrderNotFoundException {
            OrderItemDto orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);
            OrderItem orderItem = new OrderItem();
            orderItem.setId(1L);

            Product product = new Product();
            product.setId(1L);
            Order order = new Order();
            order.setId(1L);

            when(modelMapper.map(orderItemDto, OrderItem.class)).thenReturn(orderItem);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
            when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
            when(modelMapper.map(orderItem, OrderItemDto.class)).thenReturn(orderItemDto);

            OrderItemDto result = orderItemService.add(orderItemDto);

            assertNotNull(result);
            assertEquals(orderItemDto.productId, result.productId);
            verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        }

        @Test
        @DisplayName("should throw exception when order not found")
        void shouldThrowExceptionWhenOrderNotFound() {
            OrderItemDto orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);

            when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
            when(orderRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(OrderNotFoundException.class, () -> orderItemService.add(orderItemDto));

            verify(orderRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update and return order item")
        void shouldUpdateAndReturnOrderItem() throws ProductNotFoundException, OrderItemNotFoundException {
            OrderItemDto orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);
            OrderItem orderItem = new OrderItem();
            orderItem.setId(1L);

            Product product = new Product();
            product.setId(1L);

            when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
            when(modelMapper.map(orderItemDto, OrderItem.class)).thenReturn(orderItem);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
            when(modelMapper.map(orderItem, OrderItemDto.class)).thenReturn(orderItemDto);

            OrderItemDto result = orderItemService.update(orderItemDto);

            assertNotNull(result);
            assertEquals(orderItemDto.productId, result.productId);
            verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        }

        @Test
        @DisplayName("should throw exception when order item not found")
        void shouldThrowExceptionWhenOrderItemNotFound() {
            OrderItemDto orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);

            when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(OrderItemNotFoundException.class, () -> orderItemService.update(orderItemDto));

            verify(orderItemRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            OrderItemDto orderItemDto = new OrderItemDto(1L, "ProductName", 2, BigDecimal.valueOf(100.0), 1L);

            when(orderItemRepository.findById(1L)).thenReturn(Optional.of(new OrderItem()));
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> orderItemService.update(orderItemDto));

            verify(productRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete order item by id")
        void shouldDeleteOrderItemById() {
            orderItemService.delete(1L);

            verify(orderItemRepository, times(1)).deleteById(1L);
        }
    }
}
