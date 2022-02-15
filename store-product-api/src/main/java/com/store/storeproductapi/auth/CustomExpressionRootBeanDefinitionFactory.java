package com.store.storeproductapi.auth;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class CustomExpressionRootBeanDefinitionFactory {
    
    private CustomExpressionRootBeanDefinitionFactory() {

    }

    public static BeanDefinition createCustomMethodExpressionRootBeanDefinition(Class clazz) {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        
        genericBeanDefinition.setBeanClass(clazz);
        genericBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        genericBeanDefinition.setLazyInit(true);
        
        return genericBeanDefinition;
    }



}
