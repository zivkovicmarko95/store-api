package com.store.storeproductapi.controllers;

import com.store.storeproductapi.controllers.helpers.CartControllerHelper;
import com.store.storeproductapi.models.api.Cart;
import com.store.storeproductapi.models.api.CartCreate;
import com.store.storeproductapi.transferobjects.CartTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    
    private final CartControllerHelper cartControllerHelper;

    @Autowired
    public CartController(CartControllerHelper cartControllerHelper) {
        this.cartControllerHelper = cartControllerHelper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartTO> cartsCartIdGet(@PathVariable final String id) {
        
        return new ResponseEntity<>(
            cartControllerHelper.cartsCartIdGet(id),
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<CartTO> cartsPost(@RequestBody final CartCreate cartCreate) {

        return new ResponseEntity<>(
            cartControllerHelper.cartsPost(cartCreate),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/add")
    public ResponseEntity<CartTO> cartsAddPost(@RequestBody final Cart cart) {

        return new ResponseEntity<>(
            cartControllerHelper.cartsAddPost(cart),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartTO> cartsRemoveDelete(@RequestBody final Cart cart) {

        return new ResponseEntity<>(
            cartControllerHelper.cartsRemoveDelete(cart),
            HttpStatus.ACCEPTED
        );
    }

}
