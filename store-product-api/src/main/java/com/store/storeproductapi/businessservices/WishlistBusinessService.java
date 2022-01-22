package com.store.storeproductapi.businessservices;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.services.AccountService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.services.WishlistService;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class WishlistBusinessService {

    private final WishlistService wishlistService;
    private final AccountService accountService;
    private final ProductService productService;

    @Autowired
    public WishlistBusinessService(final WishlistService wishlistService, final AccountService accountService,
            final ProductService productService) {
        this.wishlistService = wishlistService;
        this.accountService = accountService;
        this.productService = productService;
    }

    /**
     * Method that creates wishlist
     * 
     * @param accountId ID of the account
     * @param productId ID of the product
     * @return Tuples of {@link WishlistModel} and {@link ProductModel}
     */
    public Tuple2<WishlistModel, ProductModel> createWishlist(final String accountId, final String productId) {
        ArgumentVerifier.verifyNotNull(accountId, productId);

        final ProductModel productModel = this.productService.findById(productId);
        final AccountModel accountModel = this.accountService.findById(accountId);
        
        final WishlistModel wishlistModel = this.wishlistService.createWishlist(accountModel.getId(), productId);

        return Tuples.of(wishlistModel, productModel);
    }

    /**
     * Add product to wishlist
     * 
     * @param wishlistId ID of the wishlist
     * @param productId  ID of the product
     * @throws ResourceStateException if quantity of the product is less then 1
     * @return Tuple of {@link WishlistModel} and {@link ProductModel}
     */
    public Tuple2<WishlistModel, ProductModel> addProductToWishlist(final String wishlistId, final String productId) {
        ArgumentVerifier.verifyNotNull(wishlistId, productId);

        final ProductModel product = this.productService.findById(productId);

        if (product.getQuantity() < 1) {
            throw new ResourceStateException(
                String.format(
                    "Product with id %s is not existing in the inventory",
                    productId
                )
            );
        }

        final WishlistModel wishlist = this.wishlistService.addProductToWishlist(wishlistId, productId);

        return Tuples.of(wishlist, product);
    }
    
}
