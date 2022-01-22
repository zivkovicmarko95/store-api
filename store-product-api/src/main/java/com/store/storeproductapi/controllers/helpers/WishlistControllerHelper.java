package com.store.storeproductapi.controllers.helpers;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.WishlistBusinessService;
import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.mappers.WishlistMapper;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.services.WishlistService;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.WishlistTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.util.function.Tuple2;

@Component
public class WishlistControllerHelper {
    
    private final WishlistBusinessService wishlistBusinessService;
    private final WishlistService wishlistService;
    private final ProductService productService;

    @Autowired
    public WishlistControllerHelper(final WishlistBusinessService wishlistBusinessService, final WishlistService wishlistService,
            final ProductService productService) {
        this.wishlistBusinessService = wishlistBusinessService;
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    // path -> /wishlists?id=param1&accountId=param2
    public WishlistTO wishlistsWishlistIdAndAccountIdGet(final String wishlistId, final String accountId) {
        
        final WishlistModel wishlist;

        if (wishlistId == null) {
            wishlist = this.wishlistService.findByAccountId(accountId);
        } else if (wishlistId != null && accountId != null) {

            final WishlistModel foundWishlist = this.wishlistService.findByAccountId(accountId);
            wishlist = this.wishlistService.findById(wishlistId);

            if (!foundWishlist.equals(wishlist)) {
                throw new ResourceStateException(
                    String.format(
                        "Wishlist with id %s and accountId %s does not exist.",
                        wishlistId, accountId
                    )
                );
            }
        } else {
            wishlist = this.wishlistService.findById(wishlistId);
        }

        final Set<ProductModel> products = wishlist.getProductIds().stream()
                .map(productId -> this.productService.findById(productId))
                .collect(Collectors.toSet());

        return WishlistMapper.mapRepoToWishlistTO(wishlist, products);
    }

    // path -> /wishlists
    public WishlistTO wishlistsPost(final String accountId, final String productId) {
        ArgumentVerifier.verifyNotNull(accountId, productId);

        final Tuple2<WishlistModel, ProductModel> wishlistAndProduct = this.wishlistBusinessService.createWishlist(accountId, productId);

        return WishlistMapper.mapRepoToWishlistTO(wishlistAndProduct.getT1(), Set.of(wishlistAndProduct.getT2()));
    }

    // path -> /wishlists/{wishlistId}/add/{productId}
    public WishlistTO wishlistsWishlistIdAddProductIdPost(final String wishlistId, final String productId) {
        ArgumentVerifier.verifyNotNull(wishlistId, productId);

        final Tuple2<WishlistModel, ProductModel> wishlistAndProduct = this.wishlistBusinessService.addProductToWishlist(wishlistId, productId);

        final Set<ProductModel> productModels = wishlistAndProduct.getT1().getProductIds().stream()
                .map(id -> this.productService.findById(id))
                .collect(Collectors.toSet());

        return WishlistMapper.mapRepoToWishlistTO(wishlistAndProduct.getT1(), productModels);
    }

    // path -> /wishlists/{wishlistId}/remove/{productId}
    public WishlistTO wishlistsWishlistIdRemoveProductIdDelete(final String wishlistId, final String productId) {
        ArgumentVerifier.verifyNotNull(wishlistId, productId);

        final WishlistModel wishlistModel = this.wishlistService.removeProductFromWishlist(wishlistId, productId);

        final Set<ProductModel> productModels = wishlistModel.getProductIds().stream()
                .map(id -> this.productService.findById(id))
                .collect(Collectors.toSet());

        return WishlistMapper.mapRepoToWishlistTO(wishlistModel, productModels);
    }

    // path -> /wishlists/{wishlistId}
    public DeleteResultTO wishlistsWishlistIdDelete(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        this.wishlistService.deleteWishlistById(id);

        return new DeleteResultTO()
                .message(
                    String.format(
                        "Wishlist with id %s is removed", id
                    )
                )
                .resourceId(id);
    }

}
