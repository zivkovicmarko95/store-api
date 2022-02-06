package com.store.storeorderapi.exceptions;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(final String msg) {
        super(msg);
    }

}
