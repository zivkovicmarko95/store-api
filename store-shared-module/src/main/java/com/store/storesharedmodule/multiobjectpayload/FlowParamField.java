package com.store.storesharedmodule.multiobjectpayload;

import java.util.Objects;

public class FlowParamField<T> {
    
    private String name;

    public FlowParamField(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FlowParamField)) {
            return false;
        }
        FlowParamField flowParamField = (FlowParamField) o;
        return Objects.equals(name, flowParamField.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            "}";
    }

}
