package com.store.storesharedmodule.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Retryable(interceptor = "MongoRetryMethod")
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StoreMongoTransaction {
    
}
