package com.agile.ecommerce.orderItem.core;

import com.agile.ecommerce.order.data.OrderRepository;
import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.data.OrderItemRepository;
import com.agile.ecommerce.orderItem.domain.OrderItem;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.orderItem.exception.OrderItemNotFoundException;
import com.agile.ecommerce.product.data.ProductRepository;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderItemService {
    private final OrderItemRepository repository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    public Page<OrderItemDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(it -> mapper.map(it, OrderItemDto.class));
    }

    public OrderItemDto getById(long id) throws OrderItemNotFoundException {
        return mapper.map(repository.findById(id).orElseThrow(() -> new OrderItemNotFoundException(id)), OrderItemDto.class);
    }

    @Transactional
    public OrderItemDto add(OrderItemDto dto) throws ProductNotFoundException, OrderNotFoundException {
        var orderItem = mapper.map(dto, OrderItem.class);
        var order = orderRepository.findById(dto.orderId).orElseThrow(() -> new OrderNotFoundException(dto.orderId));
        var product = productRepository.findById(dto.productId)
                .orElseThrow(() -> new ProductNotFoundException(dto.productId));
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        return mapper.map(repository.save(orderItem), OrderItemDto.class);
    }

    @Transactional
    public OrderItemDto update(OrderItemDto dto) throws ProductNotFoundException, OrderItemNotFoundException {
        var orderItem = repository.findById(dto.productId).orElseThrow(() -> new OrderItemNotFoundException(dto.productId));
        mapper.map(dto, orderItem);
        var product = productRepository.findById(dto.productId)
                .orElseThrow(() -> new ProductNotFoundException(dto.productId));
        orderItem.setProduct(product);
        return mapper.map(repository.save(orderItem), OrderItemDto.class);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
