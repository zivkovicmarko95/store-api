package com.store.storemanagementapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.transferobjects.EmployeeTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class EmployeeMapperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToEmployeeTO() {

        final EmployeeModel employee = PODAM_FACTORY.manufacturePojo(EmployeeModel.class);

        final EmployeeTO result = EmployeeMapper.mapRepoToEmployeeTO(employee);

        verifyEmployee(result, employee);
    }

    @Test
    void mapReposToEmployeeTOs() {

        final List<EmployeeModel> employees = PODAM_FACTORY.manufacturePojo(List.class, EmployeeModel.class);

        final List<EmployeeTO> result = EmployeeMapper.mapReposToEmployeeTOs(employees);

        IntStream.range(0, employees.size()).forEach(i -> {
            verifyEmployee(result.get(i), employees.get(i));
        });
    }

    private void verifyEmployee(final EmployeeTO result, final EmployeeModel employee) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(employee.getId());
        assertThat(result.getAddress()).isEqualTo(employee.getAddress());
        assertThat(result.getEmployeeStatus()).isEqualTo(employee.getStatus().name());
        assertThat(result.getEndOfWorkingDate()).isEqualTo(employee.getEndOfWorkingDate());
        assertThat(result.getFirstname()).isEqualTo(employee.getFirstname());
        assertThat(result.getLastname()).isEqualTo(employee.getLastname());
        assertThat(result.getPhoneNumber()).isEqualTo(employee.getPhoneNumber());
        assertThat(result.getSalary()).isEqualTo(employee.getSalary());
        assertThat(result.getStartedWorkingDate()).isEqualTo(employee.getStartedWorkingDate());
        
    }

}
