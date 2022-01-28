package com.store.storemanagementapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storemanagementapi.mappers.StoreMapper;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.services.StoreService;
import com.store.storemanagementapi.transferobjects.StoreTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest(excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class })
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, StoreController.class
})
class StoreGetMockMvcTest {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.storeService);
    }

    @Test
    void storesStoreIdGet() throws Exception {

        final StoreModel storeModel = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final String storeId = storeModel.getId();

        when(this.storeService.getById(storeId)).thenReturn(storeModel);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES_WITH_ID, storeId))
                .andExpect(status().isOk());

        final StoreTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), StoreTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(StoreMapper.mapRepoToStoreTO(storeModel));

        verify(this.storeService).getById(storeId);
    }

    @Test
    void storesStoreGet_withCityAndOpened() throws Exception {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String city = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeService.getAllOpenedByCity(city)).thenReturn(storeModels);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES)
                    .param("city", city)
                    .param("isOpen", "true"))
                .andExpect(status().isOk());

        final List<StoreTO> result = OBJECT_MAPPER.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionLikeType(List.class, StoreTO.class)
        );

        assertThat(result).isNotNull();
        assertThat(result).containsAll(StoreMapper.mapRepoStoreTOs(storeModels));

        verify(this.storeService).getAllOpenedByCity(city);
    }

    @Test
    void storesStoreGet_withCity() throws Exception {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String city = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeService.getAllByCity(city)).thenReturn(storeModels);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES)
                    .param("city", city))
                .andExpect(status().isOk());

        final List<StoreTO> result = OBJECT_MAPPER.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionLikeType(List.class, StoreTO.class)
        );

        assertThat(result).isNotNull();
        assertThat(result).containsAll(StoreMapper.mapRepoStoreTOs(storeModels));

        verify(this.storeService).getAllByCity(city);
    }

    @Test
    void storesStoreGet_noCityProvided() throws Exception {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);

        when(this.storeService.getAll()).thenReturn(storeModels);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES))
                .andExpect(status().isOk());

        final List<StoreTO> result = OBJECT_MAPPER.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionLikeType(List.class, StoreTO.class)
        );

        assertThat(result).isNotNull();
        assertThat(result).containsAll(StoreMapper.mapRepoStoreTOs(storeModels));

        verify(this.storeService).getAll();
    }

    @Test
    void storesStoreZipCodeZipCodeGet() throws Exception {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String zipCode = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeService.getAllByZipcode(zipCode)).thenReturn(storeModels);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.STORES_ZIP_CODE, zipCode))
                .andExpect(status().isOk());

        final List<StoreTO> result = OBJECT_MAPPER.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionLikeType(List.class, StoreTO.class)
        );

        assertThat(result).isNotNull();
        assertThat(result).containsAll(StoreMapper.mapRepoStoreTOs(storeModels));

        verify(this.storeService).getAllByZipcode(zipCode);
    }

}
