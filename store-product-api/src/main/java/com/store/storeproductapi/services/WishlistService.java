package com.store.storeproductapi.services;

import com.store.storeproductapi.models.WishlistModel;

public interface WishlistService {
    
    /**
     * Method that returns {@link WishlistModel} by provided wishlist ID
     * 
     * @param id ID of the wishlist
     * @return model of type {@link WishlistModel}
     */
    WishlistModel findById(final String id);

    /**
     * Method that returns {@link WishlistModel} by provided account ID
     * 
     * @param accountId ID of the account
     * @return model of type {@link WishlistModel}
     */
    WishlistModel findByAccountId(final String accountId);

    /**
     * Method that creates wishlist
     * 
     * @param accountId ID of the account
     * @param productId ID of the product
     * @return created {@link WishlistModel}
     */
    WishlistModel createWishlist(final String accountId, final String productId);

    /**
     * Method that adds product to the wishlist
     * 
     * @param id        ID of the wishlist
     * @param productId ID of the product to be added
     * @return updated {@link WishlistModel}
     */
    WishlistModel addProductToWishlist(final String id, final String productId);

    /**
     * Method that removes product from the wishlist
     * @param id        ID of the wishlist
     * @param productId ID of the product to be removed
     * @return updated {@link WishlistModel}
     */
    WishlistModel removeProductFromWishlist(final String id, final String productId);

    /**
     * Method that removes wishlist
     * 
     * @param id ID of the wishlist
     */
    void deleteWishlistById(final String id);

}
