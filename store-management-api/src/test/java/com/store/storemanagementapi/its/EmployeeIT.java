package com.store.storemanagementapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storemanagementapi.StoreManagementApiApplication;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.api.EmployeeCreate;
import com.store.storemanagementapi.models.api.EmployeeUpdate;
import com.store.storemanagementapi.repositories.EmployeeRepository;
import com.store.storemanagementapi.transferobjects.EmployeeTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class EmployeeIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final EmployeeModel EMPLOYEE_MODEL = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
    private static final String EMPLOYEE_ID = EMPLOYEE_MODEL.getId();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.employeeRepository.save(EMPLOYEE_MODEL);
    }

    @AfterEach
    void after() {
        this.employeeRepository.deleteById(EMPLOYEE_ID);
    }

    @Test
    void employeesEmployeeIdGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_ID, EMPLOYEE_ID))
                .andExpect(status().isOk());

        final EmployeeTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertEmployee(result, EMPLOYEE_MODEL);
    }

    @Test
    void employeesGetByParamFirstnameAndParamLastname() throws Exception {

        final String firstname = EMPLOYEE_MODEL.getFirstname();
        final String lastname = EMPLOYEE_MODEL.getLastname();

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_EMPLOYEES)
                    .param("firstname", firstname)
                    .param("lastname", lastname))
                .andExpect(status().isOk());

        final EmployeeTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertEmployee(result, EMPLOYEE_MODEL);
    }

    @Test
    void employeesStatusEmployeeStatusGet() throws Exception {

        final String employeeStatus = EMPLOYEE_MODEL.getStatus().name();

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_STATUS, employeeStatus))
                .andExpect(status().isOk());

        final List<EmployeeTO> result = objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), 
                TypeFactory.defaultInstance().constructCollectionLikeType(List.class, EmployeeTO.class)
        );

        assertThat(result).hasSize(1);

        result.forEach(employeeTo -> {
            assertEmployee(employeeTo, EMPLOYEE_MODEL);
        });
    }

    @Test
    void employeesPost() throws Exception {

        final EmployeeCreate employeeCreate = PODAM_FACTORY.manufacturePojo(EmployeeCreate.class);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_EMPLOYEES)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(employeeCreate)))
                .andExpect(status().isCreated());

        final EmployeeTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(employeeCreate.getAddress());
        assertThat(result.getFirstname()).isEqualTo(employeeCreate.getFirstname());
        assertThat(result.getLastname()).isEqualTo(employeeCreate.getLastname());
        assertThat(result.getPhoneNumber()).isEqualTo(employeeCreate.getPhoneNumber());
        assertThat(result.getSalary()).isEqualTo(employeeCreate.getSalary());

        this.employeeRepository.deleteById(result.getId());
    }

    @Test
    void employeesEmployeeIdInactivePost() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_ID_INACTIVE, EMPLOYEE_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        final EmployeeTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(EMPLOYEE_MODEL.getAddress());
        assertThat(result.getFirstname()).isEqualTo(EMPLOYEE_MODEL.getFirstname());
        assertThat(result.getId()).isEqualTo(EMPLOYEE_MODEL.getId());
        assertThat(result.getLastname()).isEqualTo(EMPLOYEE_MODEL.getLastname());
        assertThat(result.getPhoneNumber()).isEqualTo(EMPLOYEE_MODEL.getPhoneNumber());
        assertThat(result.getSalary()).isEqualTo(EMPLOYEE_MODEL.getSalary());
        assertThat(result.getStartedWorkingDate()).isEqualTo(EMPLOYEE_MODEL.getStartedWorkingDate());

        final EmployeeModel employeeModel = this.employeeRepository.findById(EMPLOYEE_ID).get();

        assertThat(employeeModel.getStatus()).isEqualTo(EmployeeStatusEnum.INACTIVE);
    }

    @Test
    void employeesEmployeeIdPut() throws Exception {

        final EmployeeUpdate employeeUpdate = PODAM_FACTORY.manufacturePojo(EmployeeUpdate.class)
                .id(EMPLOYEE_MODEL.getId());

        final ResultActions resultActions = this.mockMvc.perform(put(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_ID, EMPLOYEE_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(employeeUpdate)))
                .andExpect(status().isOk());

        final EmployeeTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(employeeUpdate.getAddress());
        assertThat(result.getFirstname()).isEqualTo(employeeUpdate.getFirstname());
        assertThat(result.getLastname()).isEqualTo(employeeUpdate.getLastname());
        assertThat(result.getPhoneNumber()).isEqualTo(employeeUpdate.getPhonenumber());
        assertThat(result.getSalary()).isEqualTo(employeeUpdate.getSalary());

    }

    @Test
    void employeesEmployeeIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_ID, EMPLOYEE_ID))
                .andExpect(status().isNoContent());

        final Optional<EmployeeModel> optionalEmployee = this.employeeRepository.findById(EMPLOYEE_ID);

        assertThat(optionalEmployee).isEmpty();
    }

    private void assertEmployee(EmployeeTO result, EmployeeModel employeeModel) {

        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(employeeModel.getAddress());
        assertThat(result.getEmployeeStatus()).isEqualTo(employeeModel.getStatus().name());
        assertThat(result.getEndOfWorkingDate()).isEqualTo(employeeModel.getEndOfWorkingDate());
        assertThat(result.getFirstname()).isEqualTo(employeeModel.getFirstname());
        assertThat(result.getId()).isEqualTo(employeeModel.getId());
        assertThat(result.getLastname()).isEqualTo(employeeModel.getLastname());
        assertThat(result.getPhoneNumber()).isEqualTo(employeeModel.getPhoneNumber());
        assertThat(result.getSalary()).isEqualTo(employeeModel.getSalary());
        assertThat(result.getStartedWorkingDate()).isEqualTo(employeeModel.getStartedWorkingDate());

    }

}
