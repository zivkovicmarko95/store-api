package com.store.storeproductapi.repositories;

import java.util.Optional;

import com.store.storeproductapi.models.ProductModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductModel, String> {

    Optional<ProductModel> findByTitle(String title);

}
