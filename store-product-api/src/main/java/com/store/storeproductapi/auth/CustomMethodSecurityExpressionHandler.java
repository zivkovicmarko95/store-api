package com.store.storeproductapi.auth;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    
    private CustomMethodSecurityExpressionRootRegistry customMethodSecurityExpressionRootRegistry;

    public CustomMethodSecurityExpressionHandler(CustomMethodSecurityExpressionRootRegistry customMethodSecurityExpressionRootRegistry) {
        this.customMethodSecurityExpressionRootRegistry = customMethodSecurityExpressionRootRegistry;
    }    

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
            MethodInvocation invocation) {
        
        Class<? extends CustomMethodSecurityExpressionRoot> clazz = this.customMethodSecurityExpressionRootRegistry.getRegisteredMethodExpressionRootClass();

        if (clazz != null) {

            CustomMethodSecurityExpressionRoot customMethodSecurityExpressionRoot = null;

            try {
                customMethodSecurityExpressionRoot = this.customMethodSecurityExpressionRootRegistry.manufactureConcreteClass(authentication);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            customMethodSecurityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
            customMethodSecurityExpressionRoot.setTrustResolver(getTrustResolver());
            customMethodSecurityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
            customMethodSecurityExpressionRoot.setDefaultRolePrefix(getDefaultRolePrefix());

            return customMethodSecurityExpressionRoot;

        } else {
            return super.createSecurityExpressionRoot(authentication, invocation);
        }
    }

}
