package com.store.storesharedmodule.utils;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.support.RetryTemplate;

public class MongoRetryHelper {

    private MongoRetryHelper() {

    }

    public static MethodInterceptor createMongoRetry(final RetryListener[] retryListeners, final int noOfTries, final int backOffMillisMin, final int backOffMillisMax) {

        final RetryTemplate template = new RetryTemplate();

        template.setListeners(retryListeners);
        template.setRetryPolicy(buildRetryPolicy(noOfTries));
        template.setBackOffPolicy(buildExponentialBackOffPolicy(backOffMillisMin, backOffMillisMax));

        return RetryInterceptorBuilder.stateless()
                .retryOperations(template)
                .label("Retraying transaction in mongo DB database")
                .build();
    }

    private static RetryPolicy buildRetryPolicy(int noOfTries) {
        return new StoreRetryPolicy(noOfTries, Map.of(DataAccessException.class, true), false);
    }

    private static BackOffPolicy buildExponentialBackOffPolicy(int backOffMillisMin, int backOffMillisMax) {
        
        if (backOffMillisMin > backOffMillisMax) {
            throw new IllegalStateException(
                String.format(
                    "Max retry backoff min is larger then max value. Min value is: %s, max value is: %s.",
                    backOffMillisMin, backOffMillisMax
                )
            );
        }

        final ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();

        exponentialBackOffPolicy.setInitialInterval(backOffMillisMin);
        exponentialBackOffPolicy.setMultiplier(2);
        exponentialBackOffPolicy.setMaxInterval(backOffMillisMax);
        
        return exponentialBackOffPolicy;
    }
    
}
