package com.agile.ecommerce.product.exception;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(long id) {
        super("Product "+id+" not found");
    }

    public ProductNotFoundException() {
    }
}
