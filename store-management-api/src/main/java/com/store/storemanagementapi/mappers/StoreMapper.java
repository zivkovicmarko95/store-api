package com.store.storemanagementapi.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.transferobjects.StoreTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

public class StoreMapper {
    
    private StoreMapper() {
        
    }

    public static StoreTO mapRepoToStoreTO(final StoreModel storeModel) {
        ArgumentVerifier.verifyNotNull(storeModel);

        return new StoreTO()
                .id(storeModel.getId())
                .city(storeModel.getCity())
                .phoneNumber(storeModel.getPhoneNumber())
                .storeStatus(storeModel.getStoreStatus().name())
                .street(storeModel.getStreet())
                .streetNumber(storeModel.getStreetNumber())
                .zipCode(storeModel.getZipCode());
    }

    public static List<StoreTO> mapRepoStoreTOs(final List<StoreModel> storeModels) {
        ArgumentVerifier.verifyNotNull(storeModels);

        return storeModels.stream()
                .map(StoreMapper::mapRepoToStoreTO)
                .collect(Collectors.toList());
    }

}
