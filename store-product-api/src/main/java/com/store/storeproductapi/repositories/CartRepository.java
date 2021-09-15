package com.store.storeproductapi.repositories;

import com.store.storeproductapi.models.CartModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<CartModel, String> {
    
}
