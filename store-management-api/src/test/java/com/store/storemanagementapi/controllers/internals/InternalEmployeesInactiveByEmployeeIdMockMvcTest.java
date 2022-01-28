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
import com.store.storemanagementapi.services.EmployeeService;
import com.store.storemanagementapi.transferobjects.EmployeeTO;

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
class InternalEmployeesInactiveByEmployeeIdMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.employeeService);
    }

    @Test
    void employeesEmployeeIdInactivePost() throws Exception {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String employeeId = employee.getId();

        when(this.employeeService.updateEmployeeStatusToInactive(employeeId))
                .thenReturn(employee);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_ID_INACTIVE, employeeId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        final EmployeeTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(EmployeeMapper.mapRepoToEmployeeTO(employee));

        verify(this.employeeService).updateEmployeeStatusToInactive(employeeId);
    }

}
