package com.store.storesharedmodule.utils;

import java.util.Map;

import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;

class StoreRetryPolicy extends SimpleRetryPolicy {

    private final static String STORE_IDENTITY = "STORE_IDENTITY";

    public StoreRetryPolicy(final int maxAttempts, final Map<Class< ? extends Throwable>, Boolean> retryableExceptions, final boolean cause) {
        super(maxAttempts, retryableExceptions, cause);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        final RetryContext context = super.open(parent);
        context.setAttribute(STORE_IDENTITY, STORE_IDENTITY);
        return context;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        
        if (this.isNested(context.getParent()) && !isFirstTry(context)) {
            return false;
        }

        return super.canRetry(context);
    }

    private boolean isFirstTry(RetryContext context) {
        return context.getRetryCount() == 0;
    }

    private boolean isNested(RetryContext parent) {
        if (parent == null) {
            return false;
        }

        if (parent.hasAttribute(STORE_IDENTITY)) {
            return true;
        }
        
        return isNested(parent);
    }
    
}
