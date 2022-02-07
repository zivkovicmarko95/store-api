package com.store.storeanalyticsapi.repositories;

import java.util.Optional;

import com.store.storeanalyticsapi.models.AnalyticsModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends MongoRepository<AnalyticsModel, String> {
   
    Optional<AnalyticsModel> findByAccountId(final String accountId);

}
