package com.store.storemanagementapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storemanagementapi.StoreManagementApiApplication;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.repositories.StoreRepository;
import com.store.storemanagementapi.transferobjects.StoreTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = StoreManagementApiApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc( addFilters = false )
class StoreIT {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final StoreModel STORE_MODEL = PODAM_FACTORY.manufacturePojo(StoreModel.class);
    private static final String STORE_ID = STORE_MODEL.getId();

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        this.storeRepository.save(STORE_MODEL);
    }

    @AfterEach
    void after() {
        this.storeRepository.deleteById(STORE_ID);
    }

    @Test
    void storesStoreIdGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES_WITH_ID, STORE_ID))
                .andExpect(status().isOk());

        final StoreTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), StoreTO.class);

        verifyStore(result, STORE_MODEL);
    }

    @Test
    void storesStoreZipCodeZipCodeGet() throws Exception {

        final String zipCode = STORE_MODEL.getZipCode();

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES_ZIP_CODE, zipCode))
                .andExpect(status().isOk());

        final List<StoreTO> result = objectMapper.readValue(
            resultActions.andReturn().getResponse().getContentAsString(), 
            TypeFactory.defaultInstance().constructCollectionType(List.class, StoreTO.class)
        );

        assertThat(result).hasSize(1);

        result.forEach(store -> {
            verifyStore(store, STORE_MODEL);
        });
    }

    private void verifyStore(final StoreTO result, final StoreModel storeModel) {

        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo(storeModel.getCity());
        assertThat(result.getId()).isEqualTo(storeModel.getId());
        assertThat(result.getPhoneNumber()).isEqualTo(storeModel.getPhoneNumber());
        assertThat(result.getStoreStatus()).isEqualTo(storeModel.getStoreStatus().name());
        assertThat(result.getStreet()).isEqualTo(storeModel.getStreet());
        assertThat(result.getStreetNumber()).isEqualTo(storeModel.getStreetNumber());
        assertThat(result.getZipCode()).isEqualTo(storeModel.getZipCode());

    }

}
