package com.store.storeproductapi.services.impl;

import java.util.Set;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.repositories.CartRepository;
import com.store.storeproductapi.services.CartService;
import com.store.storeproductapi.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartModel findById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return cartRepository.findById(id)
            .orElseThrow(() -> new StoreGeneralException(
                String.format(
                    "Provided id for the cart %s is not valid. Cart is not found",
                    id
                )
            ));
    }

    @Override
    public CartModel findByAccountId(final String accountId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CartModel createCart(final String productId, final String accountId, final int quantity) {
        ArgumentVerifier.verifyNotNull(productId);
        
        LOGGER.info("Creating new cart for product with productId {} and quantity {}", productId, quantity);

        final CartProductModel cartProduct = new CartProductModel(productId, quantity);
        final CartModel cart = new CartModel(accountId, Set.of(cartProduct));

        return cartRepository.insert(cart);
    }

    @Override
    public CartModel addToCart(final String cartId, final String productId, final int quantity) {
        ArgumentVerifier.verifyNotNull(cartId, productId);
        
        final CartModel cart = findById(cartId);
        final CartProductModel cartProduct = new CartProductModel(productId, quantity);

        if (!cart.getCartProducts().add(cartProduct)) {
            throw new ResourceStateException(
                String.format(
                    "It is not possible to add product %s to the cart with quantity of %d",
                    productId,
                    quantity
                )
            );
        }

        return cartRepository.save(cart);
    }

    @Override
    public void removeCart(String cartId) {
        LOGGER.info("Removing cart with id {}", cartId);
        
        final CartModel cart = findById(cartId);
        
        cartRepository.delete(cart);
    }
    
}
