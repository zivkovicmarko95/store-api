package com.store.storemanagementapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.transferobjects.InternalStoreTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class InternalStoreMapperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToInternalStoreTO() {

        final StoreModel storeModel = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final List<EmployeeModel> employeeModels = PODAM_FACTORY.manufacturePojo(List.class, EmployeeModel.class);

        final InternalStoreTO result = InternalStoreMapper.mapRepoToInternalStoreTO(storeModel, employeeModels);

        verifyInternalStore(result, storeModel);

        assertThat(result.getEmployees()).containsAll(EmployeeMapper.mapReposToEmployeeTOs(employeeModels));
    }

    private void verifyInternalStore(final InternalStoreTO result, final StoreModel storeModel) {
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(storeModel.getId());
        assertThat(result.getCity()).isEqualTo(storeModel.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(storeModel.getPhoneNumber());
        assertThat(result.getStoreStatus()).isEqualTo(storeModel.getStoreStatus().name());
        assertThat(result.getStreet()).isEqualTo(storeModel.getStreet());
        assertThat(result.getStreetNumber()).isEqualTo(storeModel.getStreetNumber());
        assertThat(result.getZipCode()).isEqualTo(storeModel.getZipCode());

    }

}
