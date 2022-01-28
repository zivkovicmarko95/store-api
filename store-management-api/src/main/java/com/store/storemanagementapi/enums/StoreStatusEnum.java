package com.store.storemanagementapi.enums;

import java.util.stream.Stream;

import com.store.storemanagementapi.exceptions.ParameterNotSupportedException;

public enum StoreStatusEnum {
    OPEN,
    CLOSED,
    TO_BE_OPENED;

    public static StoreStatusEnum resolveStoreStatus(final String storeStatus) {
        
        return Stream.of(values())
                .filter(status -> status.name().equalsIgnoreCase(storeStatus))
                .findFirst()
                .orElseThrow(() -> new ParameterNotSupportedException(
                    String.format(
                        "Provided parameter %s is not valid. Possible values are: %s",
                        storeStatus, values()
                    )
                ));
    }
}
