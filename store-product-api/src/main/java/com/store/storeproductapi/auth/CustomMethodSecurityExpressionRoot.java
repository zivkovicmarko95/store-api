package com.store.storeproductapi.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }
    
    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }
    
    @Override
    public Object getThis() {
        return this;
    }
    
    @Override
    public void setFilterObject(final Object obj) {
        this.filterObject = obj;
    }
    
    @Override
    public void setReturnObject(final Object obj) {
        this.returnObject = obj;
    }
    
}
