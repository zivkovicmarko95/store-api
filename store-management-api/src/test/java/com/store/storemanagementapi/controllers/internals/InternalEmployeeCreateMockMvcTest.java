package com.store.storemanagementapi.controllers.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storemanagementapi.constants.ApiTestConstants;
import com.store.storemanagementapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storemanagementapi.mappers.EmployeeMapper;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.api.EmployeeCreate;
import com.store.storemanagementapi.services.EmployeeService;
import com.store.storemanagementapi.transferobjects.EmployeeTO;
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
    GlobalExceptionHandler.class, InternalEmployeeController.class
})
class InternalEmployeeCreateMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.employeeService);
    }

    @Test
    void employeesPost() throws Exception {

        final EmployeeCreate employeeCreate = PODAM_FACTORY.manufacturePojo(EmployeeCreate.class);
        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);

        final String firstname = employeeCreate.getFirstname();
        final String lastname = employeeCreate.getLastname();
        final String address = employeeCreate.getAddress();
        final String phoneNumber = employeeCreate.getPhoneNumber();
        final int salary = employeeCreate.getSalary();

        when(this.employeeService.createEmployee(firstname, lastname, address, phoneNumber, salary))
                .thenReturn(employee);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_EMPLOYEES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(employeeCreate)))
                .andExpect(status().isCreated());

        final EmployeeTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(EmployeeMapper.mapRepoToEmployeeTO(employee));

        verify(this.employeeService).createEmployee(firstname, lastname, address, phoneNumber, salary);
    }

    @Test
    void employeesPost_noRequestBody() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_EMPLOYEES)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

}
