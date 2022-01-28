package com.store.storemanagementapi.repositories;

import java.util.List;

import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storemanagementapi.models.StoreModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<StoreModel, String> {
    
    List<StoreModel> findAllByCity(final String city);

    List<StoreModel> findAllByZipCode(String zipCode);
    
    List<StoreModel> findAllByStoreStatusAndCity(final StoreStatusEnum storeStatus, final String city);

    List<StoreModel> findAllByStoreStatusAndZipCode(final StoreStatusEnum storeStatusEnum, final String zipCode);

}
