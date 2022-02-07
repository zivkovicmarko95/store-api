package com.store.storeanalyticsapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(final String msg) {
        super(msg);
    }

}
