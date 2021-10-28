package com.store.storeproductapi.controllers.internals;

import java.util.Set;

import com.store.storeproductapi.controllers.helpers.CategoryControllerHelper;
import com.store.storeproductapi.models.api.CategoryCreate;
import com.store.storeproductapi.transferobjects.CategoryTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/categories")
public class InternalCategoryController {
    
    private final CategoryControllerHelper categoryControllerHelper;

    @Autowired
    public InternalCategoryController(CategoryControllerHelper categoryControllerHelper) {
        this.categoryControllerHelper = categoryControllerHelper;
    }

    @PostMapping
    public ResponseEntity<CategoryTO> internalCategoriesPost(@RequestBody final CategoryCreate categoryCreate) {

        return new ResponseEntity<>(
            categoryControllerHelper.internalCategoriesPost(categoryCreate),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/{categoryId}/assign")
    public ResponseEntity<CategoryTO> internalCategoriesCategoryIdProductsAssignPost(@PathVariable final String categoryId, 
            @RequestBody final Set<String> productIds) {
        
        return new ResponseEntity<>(
            categoryControllerHelper.internalCategoriesCategoryIdProductsAssignPost(categoryId, productIds),
            HttpStatus.OK
        );
    }

    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<CategoryTO> internalCategoriesCategoryIdProductsProductIdPost(@PathVariable final String categoryId, 
            @PathVariable final String productId) {

        return new ResponseEntity<>(
            categoryControllerHelper.internalCategoriesCategoryIdProductsProductIdPost(categoryId, productId),
            HttpStatus.OK  
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResultTO> internalCategoriesCategoryIdDelete(@PathVariable final String id) {

        return new ResponseEntity<>(
            categoryControllerHelper.internalCategoriesCategoryIdDelete(id),
            HttpStatus.OK  
        );
    }

}
