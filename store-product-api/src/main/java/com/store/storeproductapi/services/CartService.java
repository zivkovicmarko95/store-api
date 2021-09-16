package com.store.storeproductapi.services;

import com.store.storeproductapi.models.CartModel;

public interface CartService {
    
    /**
     * Method that returns {@link CartModel} by provided ID
     * 
     * @param id ID of the cart
     * @return found {@link CartModel}
     */
    CartModel findById(final String id);

    /**
     * Method that returns {@link CartModel} by account ID
     * 
     * @param accountId ID of the account
     * @return found {@link CartModel}
     */
    CartModel findByAccountId(final String accountId);

    /**
     * Method that creates product
     * 
     * @param productId ID of the product
     * @param accountId ID of the account
     * @param quantity quantity of the product
     * @return created {@link CartModel}
     */
    CartModel createCart(final String productId, final String accountId, final int quantity);

    /**
     * Method that add product to cart
     * 
     * @param cartId ID of the cart
     * @param accountId ID of the account
     * @param productId ID of the product
     * @param quantity quantity of the product
     * @return updated {@link CartModel}
     */
    CartModel addToCart(final String cartId, final String accountId, final String productId, final int quantity);

    /**
     * Method that removes product from the cart
     * 
     * @param cartId ID of the cart
     * @param accountId ID of the account
     * @param productId ID of the product
     * @return updated {@link CartModel}
     */
    CartModel removeProductFromCart(final String cartId, final String accountId, final String productId);

    /**
     * Method that removes cart
     * 
     * @param cartId ID of the cart
     */
    void removeCart(final String cartId);

}
