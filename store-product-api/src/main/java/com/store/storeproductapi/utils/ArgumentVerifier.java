package com.store.storeproductapi.utils;

import java.util.Collection;
import java.util.stream.IntStream;

import org.springframework.util.CollectionUtils;

public class ArgumentVerifier {
    
    private ArgumentVerifier() {

    }

    /**
     * Method that checks if provided object is null
     * 
     * @param objects Objects to be checked 
     * @throws RuntimeException if provided object is null
     */
    public static void verifyNotNull(Object... objects) {

        IntStream.range(0, objects.length).forEach(i -> {
            Object obj = objects[i];

            if (obj == null) {
                throw new RuntimeException(
                    "Provided parameter is empty"
                );
            }
        });
    }

    /**
     * Method that checks if provided collection is empty or null
     * 
     * @param collections Collections to be checked
     * @throws RuntimeException if provided collection is null or empty
     */
    public static void verifyNotEmpty(Collection<?>... collections) {

        IntStream.range(0, collections.length).forEach(i -> {
            Collection<?> collection = collections[i];

            if (CollectionUtils.isEmpty(collection)) {
                throw new RuntimeException(
                    "Provided collection is empty or null"
                );
            }
        });
        
    }

}
