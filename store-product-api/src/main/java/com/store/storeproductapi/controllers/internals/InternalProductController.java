package com.store.storeproductapi.controllers.internals;

import com.store.storeproductapi.controllers.helpers.ProductControllerHelper;
import com.store.storeproductapi.models.api.Product;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.ProductTO;

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
@RequestMapping("/api/internal/products")
public class InternalProductController {
    
    private final ProductControllerHelper productControllerHelper;

    @Autowired
    public InternalProductController(ProductControllerHelper productControllerHelper) {
        this.productControllerHelper = productControllerHelper;
    }

    @PostMapping
    public ResponseEntity<ProductTO> internalProductsPost(@RequestBody final Product product) {

        return new ResponseEntity<>(
            productControllerHelper.internalProductsPost(product),
            HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResultTO> internalProductsProductIdDelete(@PathVariable final String id) {

        return new ResponseEntity<>(
            productControllerHelper.internalProductsProductIdDelete(id),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<DeleteResultTO> internalProductsProductIdRemoveDelete(@PathVariable final String id) {

        return new ResponseEntity<>(
            productControllerHelper.internalProductsProductIdRemoveDelete(id),
            HttpStatus.NO_CONTENT
        );
    }

}
