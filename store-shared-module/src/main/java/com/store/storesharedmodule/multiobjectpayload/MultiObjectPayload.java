package com.store.storesharedmodule.multiobjectpayload;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.store.storesharedmodule.exceptions.MultiObjectPayloadException;

public class MultiObjectPayload {
    
    private Map<FlowParamField<?>, Object> argumentMap;

    public MultiObjectPayload() {
        this.argumentMap = new HashMap<>();
    }

    public Map<FlowParamField<?>,Object> getArgumentMap() {
        return this.argumentMap;
    }

    public <T> void addParam(FlowParamField<T> flowParamField, T paramValue) {

        if (argumentMap.containsKey(flowParamField)) {
            throw new MultiObjectPayloadException(
                String.format(
                    "Provided parameter field %s already exists in the map",
                    flowParamField.getName()
                )
            );
        }

        argumentMap.put(flowParamField, paramValue);
    }

    public <T> void replaceParam(FlowParamField<T> flowParamField, T paramValue) {

        if(!argumentMap.containsKey(flowParamField)) {
            throw new MultiObjectPayloadException(
                String.format(
                    "Provided parameter %s is not found in the map",
                    flowParamField.getName()
                )
            );
        }

        argumentMap.put(flowParamField, paramValue);
    }

    public <T> Optional<T> getOptionalObjectByKey(final FlowParamField<T> flowParamField) {

        return Optional.ofNullable(getParamValueOfArgumentMap(flowParamField));
    }

    public <T> T getObjectByKey(final FlowParamField<T> flowParamField) {
        
        Optional<T> optionalObject = getOptionalObjectByKey(flowParamField);

        if (optionalObject.isEmpty()) {
            throw new MultiObjectPayloadException(
                String.format(
                    "Not possible to fetch param %s. It does not exist in the map.",
                    flowParamField.getName()
                )
            );
        }

        return optionalObject.get();
    }

    private <T> T getParamValueOfArgumentMap(final FlowParamField<T> flowParamField) {

        try {
            return (T) argumentMap.get(flowParamField);
        } catch (ClassCastException e) {
            throw new MultiObjectPayloadException(
                String.format(
                    "Not possible to cast object type with key %s",
                    flowParamField.getName()
                )
            );
        }
    }
 
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MultiObjectPayload)) {
            return false;
        }
        MultiObjectPayload multiObjectPayload = (MultiObjectPayload) o;
        return Objects.equals(argumentMap, multiObjectPayload.argumentMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(argumentMap);
    }

    @Override
    public String toString() {
        return "{" +
            " argumentMap='" + getArgumentMap() + "'" +
            "}";
    }

}
