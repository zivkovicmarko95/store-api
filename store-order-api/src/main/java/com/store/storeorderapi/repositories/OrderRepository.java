package com.store.storeorderapi.repositories;

import com.store.storeorderapi.models.OrderModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderModel, String> {
    
}
