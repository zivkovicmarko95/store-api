package com.store.storeproductapi.repositories;

import java.util.Optional;

import com.store.storeproductapi.models.CategoryModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryModel, String> {

    Optional<CategoryModel> findByTitle(String title);

}
