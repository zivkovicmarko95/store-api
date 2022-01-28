package com.store.storemanagementapi.repositories;

import java.util.List;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storemanagementapi.models.EmployeeModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeModel, String> {
    
    EmployeeModel findByFirstnameAndLastname(final String firstname, final String lastname);

    List<EmployeeModel> findAllByStatus(final EmployeeStatusEnum status);
    
}
