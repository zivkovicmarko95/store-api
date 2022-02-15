package com.store.storeproductapi.configurations;

import com.store.storeproductapi.auth.CustomMethodSecurityExpressionHandler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CustomSecurityExpressionsAuthorizationConfig extends GlobalMethodSecurityConfiguration {
    
    private final ApplicationContext applicationContext;

    public CustomSecurityExpressionsAuthorizationConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        final PermissionEvaluator permissionEvaluator = new DenyAllPermissionEvaluator();
        final CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler();

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setApplicationContext(applicationContext);

        return expressionHandler;
    }

}
