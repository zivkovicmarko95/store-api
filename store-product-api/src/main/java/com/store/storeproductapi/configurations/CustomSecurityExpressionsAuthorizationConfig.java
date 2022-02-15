package com.store.storeproductapi.configurations;

import com.store.storeproductapi.auth.CustomMethodSecurityExpressionHandler;
import com.store.storeproductapi.auth.CustomMethodSecurityExpressionRootRegistry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityExpressionsAuthorizationConfig extends GlobalMethodSecurityConfiguration {
    
    private final ApplicationContext applicationContext;
    private final CustomMethodSecurityExpressionRootRegistry customMethodSecurityExpressionRootRegistry;

    public CustomSecurityExpressionsAuthorizationConfig(ApplicationContext applicationContext,
            CustomMethodSecurityExpressionRootRegistry customMethodSecurityExpressionRootRegistry) {
        this.applicationContext = applicationContext;
        this.customMethodSecurityExpressionRootRegistry = customMethodSecurityExpressionRootRegistry;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        final PermissionEvaluator permissionEvaluator = new DenyAllPermissionEvaluator();
        final CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler(customMethodSecurityExpressionRootRegistry);

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setApplicationContext(applicationContext);

        return expressionHandler;
    }

}

