package com.store.storeproductapi.services.impl;

import java.util.Set;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.repositories.WishlistRepository;
import com.store.storeproductapi.services.WishlistService;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final static Logger LOGGER = LoggerFactory.getLogger(WishlistServiceImpl.class);

    private final WishlistRepository wishlistRepository;

    @Autowired
    public WishlistServiceImpl(final WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public WishlistModel findById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return this.wishlistRepository.findById(id)
                .orElseThrow(() -> new StoreResourceNotFoundException(
                    String.format("Wishlist with id %s is not found.", id)
                ));
    }

    @Override
    public WishlistModel findByAccountId(final String accountId) {
        ArgumentVerifier.verifyNotNull(accountId);

        return this.wishlistRepository.findByAccountId(accountId)
                .orElseThrow(() -> new StoreResourceNotFoundException(
                    String.format("Wishlist of the account %s is not found.", accountId)
                ));
    }

    @Override
    public WishlistModel createWishlist(final String accountId, final String productId) {
        ArgumentVerifier.verifyNotNull(accountId, productId);

        final WishlistModel wishlistModel = new WishlistModel(accountId, Set.of(productId));

        LOGGER.info("Creating new wishlist {}", wishlistModel);

        return this.wishlistRepository.insert(wishlistModel);
    }

    @Override
    public WishlistModel addProductToWishlist(final String id, final String productId) {
        ArgumentVerifier.verifyNotNull(id, productId);

        final WishlistModel wishlistModel = this.findById(id);

        if (!wishlistModel.getProductIds().add(productId)) {
            throw new ResourceStateException(
                String.format("In the wishlist with id %s, product with id %s already exists.", id, productId)
            );
        }

        LOGGER.info("Adding product with ID {} to the wishlist with ID {}.", productId, id);

        return this.wishlistRepository.save(wishlistModel);
    }

    @Override
    public WishlistModel removeProductFromWishlist(final String id, final String productId) {
        ArgumentVerifier.verifyNotNull(id, productId);

        final WishlistModel wishlistModel = this.findById(id);

        if (!wishlistModel.getProductIds().remove(productId)) {
            throw new ResourceStateException(
                String.format("In the wishlist with id %s, product with id %s does not exist.", id, productId)
            );
        }

        LOGGER.info("Removing product with ID {} from the wishlist with ID {}.", productId, id);

        return this.wishlistRepository.save(wishlistModel);
    }

    @Override
    public void deleteWishlistById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        final WishlistModel wishlistModel = this.findById(id);

        this.wishlistRepository.delete(wishlistModel);
    }
    
}
