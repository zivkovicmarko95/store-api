package com.store.storemanagementapi.controllers.internals;

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
import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storemanagementapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storemanagementapi.mappers.EmployeeMapper;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.services.EmployeeService;
import com.store.storemanagementapi.transferobjects.EmployeeTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, InternalEmployeeController.class
})
class InternalEmployeeGetMockMvcTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
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
    void employeesEmployeeIdGet() throws Exception {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String employeeId = employee.getId();

        when(this.employeeService.getById(employeeId)).thenReturn(employee);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_ID, employeeId))
                .andExpect(status().isOk());

        final EmployeeTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(EmployeeMapper.mapRepoToEmployeeTO(employee));

        verify(this.employeeService).getById(employeeId);
    }

    @Test
    void employeesGetByParamFirstnameAndParamLastname() throws Exception {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String firstname = employee.getFirstname();
        final String lastname = employee.getLastname();

        when(this.employeeService.getByFirstnameAndLastName(firstname, lastname)).thenReturn(employee);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_EMPLOYEES)
                    .param("firstname", firstname)
                    .param("lastname", lastname))
                .andExpect(status().isOk());

        final EmployeeTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), EmployeeTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(EmployeeMapper.mapRepoToEmployeeTO(employee));

        verify(this.employeeService).getByFirstnameAndLastName(firstname, lastname);
    }

    @ParameterizedTest
    @EnumSource(value = EmployeeStatusEnum.class)
    void employeesStatusEmployeeStatusGet(final EmployeeStatusEnum status) throws Exception {

        final List<EmployeeModel> employees = PODAM_FACTORY.manufacturePojo(List.class, EmployeeModel.class);

        when(this.employeeService.getAllByStatus(status)).thenReturn(employees);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_EMPLOYEES_WITH_STATUS, status.name()))
                .andExpect(status().isOk());

        final List<EmployeeTO> result = OBJECT_MAPPER.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), 
                TypeFactory.defaultInstance().constructCollectionLikeType(List.class, EmployeeTO.class)
        );

        assertThat(result).isNotNull();
        assertThat(result).containsAll(EmployeeMapper.mapReposToEmployeeTOs(employees));

        verify(this.employeeService).getAllByStatus(status);

    }

}
