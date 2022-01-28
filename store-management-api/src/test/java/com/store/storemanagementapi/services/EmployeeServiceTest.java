package com.store.storemanagementapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storemanagementapi.exceptions.ResourceNotFoundException;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.repositories.EmployeeRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class EmployeeServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeService(employeeRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.employeeRepository);
    }

    @Test
    void getById() {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String id = employee.getId();

        when(this.employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        final EmployeeModel result = this.employeeService.getById(id);

        verifyEmployeeModel(result, employee);

        verify(this.employeeRepository).findById(id);
    }

    @Test
    void getById_employeeNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.employeeService.getById(id))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.employeeRepository).findById(id);
    }

    @Test
    void getAll() {
        
        final List<EmployeeModel> employees = PODAM_FACTORY.manufacturePojo(List.class, EmployeeModel.class);

        when(this.employeeRepository.findAll()).thenReturn(employees);

        final List<EmployeeModel> result = this.employeeService.getAll();

        IntStream.range(0, result.size()).forEach(i -> {
            verifyEmployeeModel(result.get(i), employees.get(i));
        });

        verify(this.employeeRepository).findAll();
    }

    @ParameterizedTest
    @EnumSource(value =  EmployeeStatusEnum.class)
    void getAllByStatus(final EmployeeStatusEnum status) {

        final List<EmployeeModel> employees = PODAM_FACTORY.manufacturePojo(List.class, EmployeeModel.class);

        when(this.employeeRepository.findAllByStatus(status)).thenReturn(employees);

        final List<EmployeeModel> result = this.employeeService.getAllByStatus(status);
        
        IntStream.range(0, result.size()).forEach(i -> {
            verifyEmployeeModel(result.get(i), employees.get(i));
        });

        verify(this.employeeRepository).findAllByStatus(status);
    }

    @Test
    void getByFirstnameAndLastName() {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String firstname = employee.getFirstname();
        final String lastname = employee.getLastname();

        when(this.employeeRepository.findByFirstnameAndLastname(firstname, lastname))
                .thenReturn(employee);

        final EmployeeModel result = this.employeeService.getByFirstnameAndLastName(firstname, lastname);

        verifyEmployeeModel(result, employee);

        verify(this.employeeRepository).findByFirstnameAndLastname(firstname, lastname);
    }

    @Test
    void createEmployee() {

        final String firstname = PODAM_FACTORY.manufacturePojo(String.class);
        final String lastname = PODAM_FACTORY.manufacturePojo(String.class);
        final String address = PODAM_FACTORY.manufacturePojo(String.class);
        final String phoneNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final int salary = PODAM_FACTORY.manufacturePojo(Integer.class);

        final EmployeeModel employee = new EmployeeModel(firstname, lastname, address, phoneNumber, salary, EmployeeStatusEnum.ACTIVE);

        when(this.employeeRepository.save(any())).thenReturn(employee);

        final EmployeeModel result = this.employeeService.createEmployee(firstname, lastname, address, phoneNumber, salary);

        verifyEmployeeModel(result, employee);

        verify(this.employeeRepository).save(any());
    }

    @Test
    void updateEmployeeStatusToInactive() {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String id = employee.getId();

        when(this.employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(this.employeeRepository.save(any())).thenReturn(employee);

        final EmployeeModel result = this.employeeService.updateEmployeeStatusToInactive(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(EmployeeStatusEnum.INACTIVE);

        verify(this.employeeRepository).findById(id);
        verify(this.employeeRepository).save(any());
    }

    @Test
    void updateEmployeeStatusToInactive_employeeNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.employeeService.updateEmployeeStatusToInactive(id))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.employeeRepository).findById(id);
    }

    @Test
    void updateEmployee() {

        final String firstname = PODAM_FACTORY.manufacturePojo(String.class);
        final String lastname = PODAM_FACTORY.manufacturePojo(String.class);
        final String address = PODAM_FACTORY.manufacturePojo(String.class);
        final String phoneNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final int salary = PODAM_FACTORY.manufacturePojo(Integer.class);
        
        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);
        final String id = employee.getId();

        when(this.employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(this.employeeRepository.save(employee)).thenReturn(employee);

        final EmployeeModel result = this.employeeService.updateEmployee(id, firstname, lastname, address, phoneNumber, salary);

        verifyEmployeeModel(result, employee);

        verify(this.employeeRepository).findById(id);
        verify(this.employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_notFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String firstname = PODAM_FACTORY.manufacturePojo(String.class);
        final String lastname = PODAM_FACTORY.manufacturePojo(String.class);
        final String address = PODAM_FACTORY.manufacturePojo(String.class);
        final String phoneNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final int salary = PODAM_FACTORY.manufacturePojo(Integer.class);

        when(this.employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.employeeService.updateEmployee(id, firstname, lastname, address, phoneNumber, salary))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.employeeRepository).findById(id);
    }

    @Test
    void removeById() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        this.employeeService.removeById(id);

        verify(this.employeeRepository).deleteById(id);
    }

    void verifyEmployeeModel(final EmployeeModel result, final EmployeeModel employee) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(employee.getId());
        assertThat(result.getAddress()).isEqualTo(employee.getAddress());
        assertThat(result.getEndOfWorkingDate()).isEqualTo(employee.getEndOfWorkingDate());
        assertThat(result.getFirstname()).isEqualTo(employee.getFirstname());
        assertThat(result.getLastname()).isEqualTo(employee.getLastname());
        assertThat(result.getPhoneNumber()).isEqualTo(employee.getPhoneNumber());
        assertThat(result.getSalary()).isEqualTo(employee.getSalary());
        assertThat(result.getStartedWorkingDate()).isEqualTo(employee.getStartedWorkingDate());
        assertThat(result.getStatus()).isEqualTo(employee.getStatus());

    }

}
