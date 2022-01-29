package com.store.storeproductapi.controllers.internals;

import com.store.storeproductapi.controllers.helpers.CartControllerHelper;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/internal/carts")
@ApiIgnore
public class InternalCartController {
    
    private final CartControllerHelper cartControllerHelper;

    @Autowired
    public InternalCartController(CartControllerHelper cartControllerHelper) {
        this.cartControllerHelper = cartControllerHelper;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResultTO> internalCartsCartIdDelete(@PathVariable final String id) {

        return new ResponseEntity<>(
            cartControllerHelper.internalCartsCartIdDelete(id),
            HttpStatus.NO_CONTENT
        );
    }

}
