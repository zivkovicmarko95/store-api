package com.store.storemanagementapi.repositories;

import com.store.storemanagementapi.models.EmployeeModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeModel, String> {
    
}
