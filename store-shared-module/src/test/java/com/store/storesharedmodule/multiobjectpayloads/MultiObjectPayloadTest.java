package com.store.storesharedmodule.multiobjectpayloads;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import com.store.storesharedmodule.exceptions.MultiObjectPayloadException;
import com.store.storesharedmodule.multiobjectpayload.FlowParamField;
import com.store.storesharedmodule.multiobjectpayload.MultiObjectPayload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class MultiObjectPayloadTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private MultiObjectPayload multiObjectPayload;

    @BeforeEach
    void setup() {
        multiObjectPayload = new MultiObjectPayload();
    }

    @Test
    @SuppressWarnings("unchecked")
    void addParam() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);
        final String paramValue = PODAM_FACTORY.manufacturePojo(String.class);

        multiObjectPayload.addParam(flowParamField, paramValue);

        assertThat(multiObjectPayload.getArgumentMap()).containsKey(flowParamField);
        assertThat(multiObjectPayload.getArgumentMap()).containsValue(paramValue);
    }

    @Test
    @SuppressWarnings("unchecked")
    void addParam_paramExists() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);
        final String paramValue = PODAM_FACTORY.manufacturePojo(String.class);

        multiObjectPayload.addParam(flowParamField, paramValue);

        assertThatThrownBy(() -> multiObjectPayload.addParam(flowParamField, paramValue))
            .isExactlyInstanceOf(MultiObjectPayloadException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided parameter field %s already exists in the map",
                    flowParamField.getName()
                )
            )
            .hasNoCause();
    }

    @Test
    @SuppressWarnings("unchecked")
    void replaceParam() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);
        final String paramValue = PODAM_FACTORY.manufacturePojo(String.class);
        final String paramValueToReplace = PODAM_FACTORY.manufacturePojo(String.class);

        multiObjectPayload.getArgumentMap().put(flowParamField, paramValue);

        multiObjectPayload.replaceParam(flowParamField, paramValueToReplace);

        assertThat(multiObjectPayload.getArgumentMap()).containsKey(flowParamField);
        assertThat(multiObjectPayload.getArgumentMap()).isNotIn(paramValue);
        assertThat(multiObjectPayload.getArgumentMap()).containsValue(paramValueToReplace);
    }

    @Test
    @SuppressWarnings("unchecked")
    void replaceParam_paramNotExist() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);
        final String paramValue = PODAM_FACTORY.manufacturePojo(String.class);

        assertThatThrownBy(() -> multiObjectPayload.replaceParam(flowParamField, paramValue))
            .isExactlyInstanceOf(MultiObjectPayloadException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided parameter %s is not found in the map",
                    flowParamField.getName()
                )
            )
            .hasNoCause();
    }

    @Test
    @SuppressWarnings("unchecked")
    void getOptionalObjectByKey() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);
        final String paramValue = PODAM_FACTORY.manufacturePojo(String.class);
        multiObjectPayload.getArgumentMap().put(flowParamField, paramValue);

        final Optional<String> result = multiObjectPayload.getOptionalObjectByKey(flowParamField);

        assertThat(result).isNotEmpty();
        assertThat(result).contains(paramValue);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getObjectByKey() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);
        final String paramValue = PODAM_FACTORY.manufacturePojo(String.class);
        multiObjectPayload.getArgumentMap().put(flowParamField, paramValue);

        final String result = multiObjectPayload.getObjectByKey(flowParamField);

        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(paramValue);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getObjectByKey_paramNotExist() {

        final FlowParamField<String> flowParamField = PODAM_FACTORY.manufacturePojo(FlowParamField.class, String.class);

        assertThatThrownBy(() -> this.multiObjectPayload.getObjectByKey(flowParamField))
            .isExactlyInstanceOf(MultiObjectPayloadException.class)
            .hasMessageStartingWith(
                String.format(
                    "Not possible to fetch param %s. It does not exist in the map.",
                    flowParamField.getName()
                )
            )
            .hasNoCause();
    }

}
