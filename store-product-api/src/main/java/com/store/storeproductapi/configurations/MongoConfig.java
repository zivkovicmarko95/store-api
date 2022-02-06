package com.store.storeproductapi.configurations;

import com.store.storesharedmodule.utils.MongoRetryHelper;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class MongoConfig {
    
    @Autowired
    private Environment env;

    @Bean(name = "MongoRetryMethod")
    MethodInterceptor mongoRetryMethod(final RetryListener[] retryListeners) {
        
        final int noOfTries = Integer.parseInt(env.getProperty("store.mongo.noOfTries"));
        final int backOffMillisMin = Integer.parseInt(env.getProperty("store.mongo.backOffMillisMin"));
        final int backOffMillisMax = Integer.parseInt(env.getProperty("store.mongo.backOffMillisMax"));

        return MongoRetryHelper.createMongoRetry(retryListeners, noOfTries, backOffMillisMin, backOffMillisMax);
    }

}
