package com.store.storeproductapi.repositories;

import java.util.Optional;

import com.store.storeproductapi.models.AccountModel;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<AccountModel, String> {
    
    Optional<AccountModel> findByUsername(final String username);
    
    Optional<AccountModel> findBySubjectId(final String subjectId);

}
