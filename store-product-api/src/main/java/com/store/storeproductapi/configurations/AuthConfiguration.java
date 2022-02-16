package com.store.storeproductapi.configurations;

import com.store.storeproductapi.auth.CustomMethodSecurityExpressionRootRegistry;
import com.store.storeproductapi.auth.StoreCustomExpressionRoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    
    @Autowired
    public void configureMethodSecurity(CustomMethodSecurityExpressionRootRegistry customMethodSecurityExpressionRootRegistry) {

        customMethodSecurityExpressionRootRegistry.registerRootClass(StoreCustomExpressionRoot.class);
    }
}
