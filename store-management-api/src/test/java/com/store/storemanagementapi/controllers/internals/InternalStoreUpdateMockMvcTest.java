package com.store.storemanagementapi.controllers.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storemanagementapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.models.api.StoreUpdate;
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
class InternalStoreUpdateMockMvcTest {
    
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
    void internalStoresPut() throws Exception {

        final StoreUpdate storeUpdate = PODAM_FACTORY.manufacturePojo(StoreUpdate.class)
                .status("OPEN");
        final StoreModel storeModel = PODAM_FACTORY.manufacturePojo(StoreModel.class);

        final String id = storeUpdate.getId();
        final String city = storeUpdate.getCity();
        final String street = storeUpdate.getStreet();
        final String streetNumber = storeUpdate.getStreetNumber();
        final String phoneNumber = storeUpdate.getPhoneNumber();
        final String zipcode = storeUpdate.getZipcode();
        final StoreStatusEnum status = StoreStatusEnum.resolveStoreStatus(storeUpdate.getStatus());

        when(this.storeService.updateStore(id, city, street, streetNumber, phoneNumber, zipcode, status))
                .thenReturn(storeModel);
        storeModel.getEmployeeIds().forEach(employeeIds -> {
            when(this.employeeService.getById(employeeIds)).thenReturn(PODAM_FACTORY.manufacturePojo(EmployeeModel.class));
        });

        final ResultActions resultActions = this.mockMvc.perform(put(ApiTestConstants.INTERNAL_STORES)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)    
                    .content(OBJECT_MAPPER.writeValueAsString(storeUpdate)))
                .andExpect(status().isAccepted());

        final InternalStoreTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), InternalStoreTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(storeModel.getId());

        verify(this.storeService).updateStore(id, city, street, streetNumber, phoneNumber, zipcode, status);
        storeModel.getEmployeeIds().forEach(employeeId -> verify(this.employeeService).getById(employeeId));
    }

    @Test
    void internalStoresPut_noRequestBody() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(put(ApiTestConstants.INTERNAL_STORES)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

}
