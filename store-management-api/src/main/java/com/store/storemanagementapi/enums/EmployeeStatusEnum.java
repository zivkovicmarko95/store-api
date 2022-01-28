package com.store.storemanagementapi.enums;

import java.util.stream.Stream;

import com.store.storemanagementapi.exceptions.ParameterNotSupportedException;

public enum EmployeeStatusEnum {
    ACTIVE,
    FIRED,
    INACTIVE;

    public static EmployeeStatusEnum resolveEmployeeStatus(final String employeeStatus) {
        
        return Stream.of(values())
                .filter(status -> status.name().equalsIgnoreCase(employeeStatus))
                .findFirst()
                .orElseThrow(() -> new ParameterNotSupportedException(
                    String.format(
                        "Provided parameter %s is not valid. Possible values are: %s",
                        employeeStatus, values()
                    )
                ));
    }
}
