package com.agile.ecommerce.order.core;

import com.agile.ecommerce.order.data.OrderRepository;
import com.agile.ecommerce.order.domain.Order;
import com.agile.ecommerce.order.dto.OrderDto;
import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.domain.OrderItem;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.product.data.ProductRepository;
import com.agile.ecommerce.product.domain.Product;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public Page<OrderDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(it->mapper.map(it, OrderDto.class));
    }

    public OrderDto getById(long id) throws OrderNotFoundException {
        return mapper.map(repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id)), OrderDto.class);
    }

    @Transactional
    public OrderDto add(OrderDto dto) throws ProductNotFoundException {
        var order = mapper.map(dto, Order.class);
        if (dto.orderItems() != null) {
            for (var item : dto.orderItems()) {
                var product = productRepository.findById(item.productId)
                        .orElseThrow(() -> new ProductNotFoundException(item.productId));
                order.getOrderItems().clear();
                order.getOrderItems().add(new OrderItem(null, order, product, item.quantity, item.price));
            }
        }
        var sv = repository.save(order);
        return new OrderDto(sv.getId(),
                sv.getOrderDate(),
                sv.getCustomerName(),
                sv.getCustomerAddress(),
                sv.getOrderItems().stream().map(it-> mapper.map(it, OrderItemDto.class)).toList());
    }

    @Transactional
    public OrderDto update(OrderDto dto) throws OrderNotFoundException, ProductNotFoundException {
        if (!repository.existsById(dto.id())) {
            throw new OrderNotFoundException(dto.id());
        }
        var order = mapper.map(dto, Order.class);
        if (order.getOrderItems() != null) {
            for (var item : order.getOrderItems()) {
                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new ProductNotFoundException(item.getProduct().getId()));
                item.setProduct(product);
                item.setOrder(order);
            }
        }
        var sv = repository.save(order);
        return new OrderDto(sv.getId(),
                sv.getOrderDate(),
                sv.getCustomerName(),
                sv.getCustomerAddress(),
                sv.getOrderItems().stream().map(it-> mapper.map(it, OrderItemDto.class)).toList());
    }

    @Transactional
    public void delete(long id) throws OrderNotFoundException {
        if (!repository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
