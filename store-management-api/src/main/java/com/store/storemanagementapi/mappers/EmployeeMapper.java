package com.store.storemanagementapi.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.transferobjects.EmployeeTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

public class EmployeeMapper {
    
    private EmployeeMapper() {
        
    }

    public static EmployeeTO mapRepoToEmployeeTO(final EmployeeModel employeeModel) {
        ArgumentVerifier.verifyNotNull(employeeModel);

        return new EmployeeTO()
                .id(employeeModel.getId())
                .firstname(employeeModel.getFirstname())
                .lastname(employeeModel.getLastname())
                .address(employeeModel.getAddress())
                .phoneNumber(employeeModel.getPhoneNumber())
                .salary(employeeModel.getSalary())
                .employeeStatus(employeeModel.getStatus().name())
                .startedWorkingDate(employeeModel.getStartedWorkingDate())
                .endOfWorkingDate(employeeModel.getEndOfWorkingDate());
    }

    public static List<EmployeeTO> mapReposToEmployeeTOs(final List<EmployeeModel> employeeModels) {
        ArgumentVerifier.verifyNotNull(employeeModels);

        return employeeModels.stream()
                .map(EmployeeMapper::mapRepoToEmployeeTO)
                .collect(Collectors.toList());
    }

}
