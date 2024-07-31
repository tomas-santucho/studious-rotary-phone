package com.agile.ecommerce.orderItem.data;

import com.agile.ecommerce.orderItem.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
