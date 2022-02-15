package com.store.storeproductapi.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomMethodSecurityExpressionRootRegistry {
    
    @Autowired
    private ApplicationContext applicationContext;

    private Class<? extends CustomMethodSecurityExpressionRoot> methodSecurityExpressionRootClass;

    public void registerRootClass(Class<? extends CustomMethodSecurityExpressionRoot> clazz) {
        this.methodSecurityExpressionRootClass = clazz;

        //register prototype bean definition for custom method security
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) this.applicationContext.getAutowireCapableBeanFactory();
        
        final BeanDefinition methodExpressionRootBeanDefinition =
                CustomExpressionRootBeanDefinitionFactory.createCustomMethodExpressionRootBeanDefinition(this.methodSecurityExpressionRootClass);
        beanDefinitionRegistry.registerBeanDefinition(this.methodSecurityExpressionRootClass.getSimpleName(), methodExpressionRootBeanDefinition);
    }

    public Class<? extends CustomMethodSecurityExpressionRoot> getRegisteredMethodExpressionRootClass() {
        return this.methodSecurityExpressionRootClass;
    }

    public CustomMethodSecurityExpressionRoot manufactureConcreteClass(Authentication authentication) {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) this.applicationContext.getAutowireCapableBeanFactory();

        if (this.methodSecurityExpressionRootClass == null || !beanDefinitionRegistry.containsBeanDefinition(this.methodSecurityExpressionRootClass.getSimpleName())) {
            throw new RuntimeException("No custom security expression class.");
        }

        return this.applicationContext.getBeanProvider(this.methodSecurityExpressionRootClass).getObject(authentication);
    }

}
