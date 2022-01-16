package com.store.storeproductapi.controllers;

import java.util.Set;

import com.store.storeproductapi.controllers.helpers.CategoryControllerHelper;
import com.store.storeproductapi.transferobjects.CategoryTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    private final CategoryControllerHelper categoryControllerHelper;

    @Autowired
    public CategoryController(CategoryControllerHelper categoryControllerHelper) {
        this.categoryControllerHelper = categoryControllerHelper;
    }

    @GetMapping
    public ResponseEntity<Set<CategoryTO>> categoriesGet() {
        
        return new ResponseEntity<>(
            categoryControllerHelper.categoriesGet(),
            HttpStatus.OK  
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryTO> categoriesCategoryIdGet(@PathVariable final String categoryId) {

        return new ResponseEntity<>(
            categoryControllerHelper.categoriesCategoryIdGet(categoryId),
            HttpStatus.OK
        );
    }

}
