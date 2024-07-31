package com.agile.ecommerce.config;

import com.agile.ecommerce.order.domain.Order;
import com.agile.ecommerce.order.dto.OrderDto;
import com.agile.ecommerce.orderItem.domain.OrderItem;
import com.agile.ecommerce.orderItem.dto.OrderItemDto;
import com.agile.ecommerce.product.domain.Product;
import com.agile.ecommerce.product.dto.ProductDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.addConverter(orderItemToOrderItemDtoConverter());
        modelMapper.addConverter(orderToOrderDtoConverter(modelMapper));
        modelMapper.addConverter(productToProductDtoConverter());

        return modelMapper;
    }

    private Converter<OrderItem, OrderItemDto> orderItemToOrderItemDtoConverter() {
        return context -> {
            var source = context.getSource();
            return new OrderItemDto(
                    source.getProduct().getId(),
                    source.getProduct().getName(),
                    source.getQuantity(),
                    source.getPrice(),
                    source.getOrder().getId()
            );
        };
    }

    private Converter<Order, OrderDto> orderToOrderDtoConverter(ModelMapper modelMapper) {
        return context -> {
            var source = context.getSource();
            List<OrderItemDto> orderItemDtos = source.getOrderItems().stream()
                    .map(item -> modelMapper.map(item, OrderItemDto.class))
                    .collect(Collectors.toList());
            return new OrderDto(
                    source.getId(),
                    source.getOrderDate(),
                    source.getCustomerName(),
                    source.getCustomerAddress(),
                    orderItemDtos
            );
        };
    }

    private Converter<Product, ProductDto> productToProductDtoConverter() {
        return context -> {
            var source = context.getSource();
            return new ProductDto(
                    source.getId(),
                    source.getName(),
                    source.getDescription(),
                    source.getPrice(),
                    source.getQuantity()
            );
        };
    }
}
