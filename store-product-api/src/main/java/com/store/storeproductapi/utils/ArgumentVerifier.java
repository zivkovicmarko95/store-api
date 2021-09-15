package com.store.storeproductapi.utils;

import org.apache.commons.lang3.StringUtils;

public class ArgumentVerifier {
    
    private ArgumentVerifier() {

    }

    public static void verifyNotNull(String... objects) {

        if (StringUtils.isAnyEmpty(objects)) {
            throw new RuntimeException(
                "Provided parameter is empty"
            );
        }
    }

}
