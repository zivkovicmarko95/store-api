package com.store.storemanagementapi.enums;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.store.storemanagementapi.exceptions.ParameterNotSupportedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class StoreStatusEnumTest {

    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @ParameterizedTest
    @EnumSource(value = StoreStatusEnum.class)
    void resolveStoreStatus(final StoreStatusEnum status) {

        assertThat(StoreStatusEnum.resolveStoreStatus(status.toString())).isEqualTo(status);
    }

    @Test
    void resolveStoreStatus_notSupported() {

        final String status = PODAM_FACTORY.manufacturePojo(String.class);

        assertThatThrownBy(() -> StoreStatusEnum.resolveStoreStatus(status))
                .isExactlyInstanceOf(ParameterNotSupportedException.class)
                .hasMessageStartingWith(
                    String.format("Provided parameter %s is not valid.", status)
                )
                .hasNoCause();
    }

}
