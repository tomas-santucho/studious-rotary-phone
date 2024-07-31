package com.agile.ecommerce.orderItem.exception;

public class OrderItemNotFoundException extends Exception{
    public OrderItemNotFoundException(long id) {
        super("OrderItem "+id+" not found.");
    }
}
