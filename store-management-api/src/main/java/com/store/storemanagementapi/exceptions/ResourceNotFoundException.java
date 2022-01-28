package com.store.storemanagementapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(final String msg) {
        super(msg);
    }

}
