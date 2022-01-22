package com.store.storeproductapi.repositories;

import java.util.Optional;

import com.store.storeproductapi.models.WishlistModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends MongoRepository<WishlistModel, String> {
    
    Optional<WishlistModel> findByAccountId(final String accountId);

}
