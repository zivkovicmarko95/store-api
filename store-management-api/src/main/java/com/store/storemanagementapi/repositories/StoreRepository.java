package com.store.storemanagementapi.repositories;

import com.store.storemanagementapi.models.StoreModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<StoreModel, String> {
    
}
