package com.store.storeproductapi.controllers;

import java.security.InvalidParameterException;

import com.store.storeproductapi.controllers.helpers.WishlistControllerHelper;
import com.store.storeproductapi.models.api.WishlistCreate;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.WishlistTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {
    
    private final WishlistControllerHelper wishlistControllerHelper;

    @Autowired
    public WishlistController(WishlistControllerHelper wishlistControllerHelper) {
        this.wishlistControllerHelper = wishlistControllerHelper;
    }

    @GetMapping
    public ResponseEntity<WishlistTO> wishlistsWishlistIdAndAccountIdGet(@RequestParam(required = false) final String wishlistId,
            @RequestParam(required = false) final String accountId) {

        if (wishlistId == null && accountId == null) {
            throw new InvalidParameterException(
                String.format(
                    "Invalid parameters. Please provide wishlist ID or account ID, or both parameters."
                )
            );
        }

        return new ResponseEntity<>(
            wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(wishlistId, accountId),
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<WishlistTO> wishlistsPost(@RequestBody final WishlistCreate wishlist) {

        return new ResponseEntity<>( 
            this.wishlistControllerHelper.wishlistsPost(wishlist.getAccountId(), wishlist.getProductId()),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/{wishlistId}/add/{productId}")
    public ResponseEntity<WishlistTO> wishlistsWishlistIdAddProductIdPost(@PathVariable final String wishlistId,
            @PathVariable final String productId) {

        return new ResponseEntity<>(
            this.wishlistControllerHelper.wishlistsWishlistIdAddProductIdPost(wishlistId, productId),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<DeleteResultTO> wishlistsWishlistIdDelete(@PathVariable final String wishlistId) {

        return new ResponseEntity<>(
            this.wishlistControllerHelper.wishlistsWishlistIdDelete(wishlistId),
            HttpStatus.NO_CONTENT
        );
    }

    @DeleteMapping("/{wishlistId}/remove/{productId}")
    public ResponseEntity<WishlistTO> wishlistsWishlistIdRemoveProductIdDelete(@PathVariable final String wishlistId,
            @PathVariable final String productId) {

        return new ResponseEntity<>(
            this.wishlistControllerHelper.wishlistsWishlistIdRemoveProductIdDelete(wishlistId, productId),
            HttpStatus.OK
        );
    }

}
