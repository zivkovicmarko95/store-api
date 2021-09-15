package com.store.storeproductapi.businessservices;

import com.store.storeproductapi.services.CartService;
import com.store.storeproductapi.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartBusinessService {
    
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartBusinessService(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }


}
