package com.store.storemanagementapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.transferobjects.StoreTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class StoreMapperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToStoreTO() {

        final StoreModel storeModel = PODAM_FACTORY.manufacturePojo(StoreModel.class);

        final StoreTO result = StoreMapper.mapRepoToStoreTO(storeModel);

        verifyStore(result, storeModel);
    }

    @Test
    void mapRepoStoreTOs() {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);

        final List<StoreTO> result = StoreMapper.mapRepoStoreTOs(storeModels);

        IntStream.range(0, storeModels.size()).forEach(i -> {
            verifyStore(result.get(i), storeModels.get(i));
        });
    }

    private void verifyStore(final StoreTO result, final StoreModel storeModel) {
        
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
