package com.store.storeproductapi.controllers;

import java.util.Set;

import com.store.storeproductapi.controllers.helpers.ProductControllerHelper;
import com.store.storeproductapi.transferobjects.ProductTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductControllerHelper productControllerHelper;

    @Autowired
    public ProductController(ProductControllerHelper productControllerHelper) {
        this.productControllerHelper = productControllerHelper;
    }

    @GetMapping("/search")
    public ResponseEntity<ProductTO> productsProductIdAndTitleGet(@RequestParam(required = false) final String id, 
            @RequestParam(required = false) final String title) {

        return new ResponseEntity<>(
            productControllerHelper.productsProductIdAndTitleGet(id, title),
            HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<Set<ProductTO>> productsGet(@RequestParam final String page) {
        
        final int requestedPage = Integer.parseInt(page);

        return new ResponseEntity<>(
            productControllerHelper.productsGet(requestedPage),
            HttpStatus.OK
        );
    }

}
