package com.store.storemanagementapi.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.store.storemanagementapi.exceptions.ParameterNotSupportedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class EmployeeStatusEnumTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @ParameterizedTest
    @EnumSource(value = EmployeeStatusEnum.class)
    void resolveEmployeeStatus(final EmployeeStatusEnum status) {

        assertThat(EmployeeStatusEnum.resolveEmployeeStatus(status.toString())).isEqualTo(status);
    }

    @Test
    void resolveEmployeeStatus_notSupported() {

        final String status = PODAM_FACTORY.manufacturePojo(String.class);

        assertThatThrownBy(() -> EmployeeStatusEnum.resolveEmployeeStatus(status))
                .isExactlyInstanceOf(ParameterNotSupportedException.class)
                .hasMessageStartingWith(
                    String.format("Provided parameter %s is not valid.", status)
                )
                .hasNoCause();
    }

}
