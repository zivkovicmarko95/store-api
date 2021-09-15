package com.store.storeproductapi.repositories;

import java.util.Optional;

import com.store.storeproductapi.models.CartModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<CartModel, String> {
    
    Optional<CartModel> findByAccountId(String accountId);

}
