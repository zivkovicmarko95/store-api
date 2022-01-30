package com.store.storemanagementapi.its.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storemanagementapi.StoreManagementApiApplication;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storemanagementapi.mappers.EmployeeMapper;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.models.api.StoreCreate;
import com.store.storemanagementapi.models.api.StoreUpdate;
import com.store.storemanagementapi.repositories.EmployeeRepository;
import com.store.storemanagementapi.repositories.StoreRepository;
import com.store.storemanagementapi.transferobjects.InternalStoreTO;
import com.store.storesharedmodule.models.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class InternalStoreIT {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private final static StoreModel STORE_MODEL = PODAM_FACTORY.manufacturePojo(StoreModel.class);
    private final static List<EmployeeModel> EMPLOYEE_MODELS = STORE_MODEL.getEmployeeIds().stream()
            .map(employeeId -> PODAM_FACTORY.manufacturePojo(EmployeeModel.class).id(employeeId))
            .collect(Collectors.toList());

    private final static String STORE_ID = STORE_MODEL.getId();

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.storeRepository.save(STORE_MODEL);
        this.employeeRepository.saveAll(EMPLOYEE_MODELS);
    }

    @AfterEach
    void after() {
        this.storeRepository.deleteById(STORE_ID);
        this.employeeRepository.deleteAll(EMPLOYEE_MODELS);
    }

    @Test
    void internalStoresPost() throws Exception {

        final StoreCreate storeCreate = PODAM_FACTORY.manufacturePojo(StoreCreate.class);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_STORES)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(storeCreate)))
                .andExpect(status().isCreated());

        final InternalStoreTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), InternalStoreTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo(storeCreate.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(storeCreate.getPhoneNumber());
        assertThat(result.getStreet()).isEqualTo(storeCreate.getStreet());
        assertThat(result.getStreetNumber()).isEqualTo(storeCreate.getStreetNumber());
        assertThat(result.getZipCode()).isEqualTo(storeCreate.getZipcode());
        assertThat(result.getEmployees()).isEmpty();

        this.storeRepository.deleteById(result.getId());
    }

    @Test
    void internalStoresPost_noRequestBody() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_STORES)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        final HttpResponse response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

    @ParameterizedTest
    @EnumSource(value = StoreStatusEnum.class)
    void internalStoresPut(final StoreStatusEnum status) throws Exception {

        final StoreUpdate storeUpdate = PODAM_FACTORY.manufacturePojo(StoreUpdate.class)
                .status(status.toString());

        final ResultActions resultActions = this.mockMvc.perform(put(ApiTestConstants.INTERNAL_STORES_WITH_ID, STORE_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(storeUpdate)))
                .andExpect(status().isAccepted());

        final InternalStoreTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), InternalStoreTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(STORE_ID);
        assertThat(result.getCity()).isEqualTo(storeUpdate.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(storeUpdate.getPhoneNumber());
        assertThat(result.getStoreStatus()).isEqualTo(storeUpdate.getStatus());
        assertThat(result.getStreet()).isEqualTo(storeUpdate.getStreet());
        assertThat(result.getStreetNumber()).isEqualTo(storeUpdate.getStreetNumber());
        assertThat(result.getZipCode()).isEqualTo(storeUpdate.getZipcode());
        assertThat(result.getEmployees()).containsAll(EmployeeMapper.mapReposToEmployeeTOs(EMPLOYEE_MODELS));
    }

    @Test
    void internalStoresPut_noRequestBody() throws Exception {

        final String storeId = PODAM_FACTORY.manufacturePojo(String.class);

        final ResultActions resultActions = this.mockMvc.perform(put(ApiTestConstants.INTERNAL_STORES_WITH_ID, storeId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        final HttpResponse response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

}
