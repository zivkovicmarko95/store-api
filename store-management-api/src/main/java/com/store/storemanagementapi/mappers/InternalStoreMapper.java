package com.store.storemanagementapi.mappers;

import java.util.List;

import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.transferobjects.InternalStoreTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

public class InternalStoreMapper {
    
    private InternalStoreMapper() {

    }

    public static InternalStoreTO mapRepoToInternalStoreTO(final StoreModel storeModel, final List<EmployeeModel> employeeModels) {
        ArgumentVerifier.verifyNotNull(storeModel, employeeModels);
        
        final String id = storeModel.getId();
        final String city = storeModel.getCity();
        final String street = storeModel.getStreet();
        final String streetNumber = storeModel.getStreetNumber();
        final String phoneNumber = storeModel.getPhoneNumber();
        final String zipCode = storeModel.getZipCode();
        final String storeStatus = storeModel.getStoreStatus().name();
        
        return new InternalStoreTO(id, city, street, streetNumber, phoneNumber, zipCode, storeStatus, 
                EmployeeMapper.mapReposToEmployeeTOs(employeeModels));
    }
}
