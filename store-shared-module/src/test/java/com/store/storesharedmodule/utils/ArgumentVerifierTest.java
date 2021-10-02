package com.store.storesharedmodule.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.store.storesharedmodule.exceptions.StoreArgumentException;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class ArgumentVerifierTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void verifyNotNull() {

        final String argument1 = PODAM_FACTORY.manufacturePojo(String.class);
        final String argument2 = PODAM_FACTORY.manufacturePojo(String.class);
        final String argument3 = PODAM_FACTORY.manufacturePojo(String.class);

        ArgumentVerifier.verifyNotNull(argument1, argument2, argument3);
    }

    @Test
    void verifyNotNull_nullParameter() {

        final String argument1 = PODAM_FACTORY.manufacturePojo(String.class);
        final String argument2 = "";
        final String argument3 = null;

        assertThatThrownBy(() -> ArgumentVerifier.verifyNotNull(argument1, argument2, argument3))
            .isExactlyInstanceOf(StoreArgumentException.class)
            .hasNoCause();
    }

    @Test
    void verifyNotEmpty() {

        final List<Object> list = PODAM_FACTORY.manufacturePojo(List.class, Object.class);
        final Set<Object> set = PODAM_FACTORY.manufacturePojo(Set.class, Object.class);
        final Collection<Object> collection = PODAM_FACTORY.manufacturePojo(Collection.class, Object.class);

        ArgumentVerifier.verifyNotEmpty(list, set, collection);
    }

    @Test
    void verifyNotEmpty_emptyList() {

        final Set<Object> set = PODAM_FACTORY.manufacturePojo(Set.class, Object.class);
        final Collection<Object> collection = PODAM_FACTORY.manufacturePojo(Collection.class, Object.class);
        final List<Object> list = new ArrayList<>();
        
        assertThatThrownBy(() -> ArgumentVerifier.verifyNotEmpty(set, collection, list))
            .isExactlyInstanceOf(StoreArgumentException.class)
            .hasNoCause();
    }

    @Test
    void verifyNotEmpty_nullList() {

        final Set<Object> set = PODAM_FACTORY.manufacturePojo(Set.class, Object.class);
        final Collection<Object> collection = PODAM_FACTORY.manufacturePojo(Collection.class, Object.class);
        final List<Object> list = null;
        
        assertThatThrownBy(() -> ArgumentVerifier.verifyNotEmpty(set, collection, list))
            .isExactlyInstanceOf(StoreArgumentException.class)
            .hasNoCause();
    }

}
