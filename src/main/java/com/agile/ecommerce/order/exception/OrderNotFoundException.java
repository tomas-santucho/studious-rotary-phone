package com.agile.ecommerce.order.exception;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(long id) {
        super("Order "+id+" not found.");
    }
}
