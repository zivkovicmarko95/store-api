package com.store.storemanagementapi.controllers.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.models.api.StoreCreate;
import com.store.storemanagementapi.services.EmployeeService;
import com.store.storemanagementapi.services.StoreService;
import com.store.storemanagementapi.transferobjects.InternalStoreTO;
import com.store.storesharedmodule.models.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest(excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class })
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, InternalStoreController.class
})
class InternalStoreCreateMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private EmployeeService employeeService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.storeService, this.employeeService);
    }

    @Test
    void internalStoresPost() throws Exception {

        final StoreCreate storeCreate = PODAM_FACTORY.manufacturePojo(StoreCreate.class);
        final StoreModel storeModel = PODAM_FACTORY.manufacturePojo(StoreModel.class);

        final String city = storeCreate.getCity();
        final String street = storeCreate.getStreet();
        final String streetNumber = storeCreate.getStreetNumber();
        final String phoneNumber = storeCreate.getPhoneNumber();
        final String zipcode = storeCreate.getZipcode();

        when(this.storeService.createStore(city, street, streetNumber, phoneNumber, zipcode))
                .thenReturn(storeModel);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_STORES)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)    
                    .content(OBJECT_MAPPER.writeValueAsString(storeCreate)))
                .andExpect(status().isCreated());

        final InternalStoreTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), InternalStoreTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(storeModel.getId());
        assertThat(result.getEmployees()).isEqualTo(List.of());

        verify(this.storeService).createStore(city, street, streetNumber, phoneNumber, zipcode);
    }

    @Test
    void internalStoresPost_noRequestBody() throws Exception {
        
        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_STORES)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
        
    }

}
