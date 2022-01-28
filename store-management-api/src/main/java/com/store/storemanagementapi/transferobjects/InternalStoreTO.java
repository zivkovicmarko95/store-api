package com.store.storemanagementapi.transferobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InternalStoreTO extends StoreTO {
    
    private List<EmployeeTO> employees = new ArrayList<>();

    public InternalStoreTO() {

    }

    public InternalStoreTO(String id, String city, String street, String streetNumber, String phoneNumber, 
            String zipCode, String storeStatus, List<EmployeeTO> employees) {
        super(id, city, street, streetNumber, phoneNumber, zipCode, storeStatus);
        this.employees = employees;
    }

    public List<EmployeeTO> getEmployees() {
        return this.employees;
    }

    public void setEmployees(List<EmployeeTO> employees) {
        this.employees = employees;
    }

    public InternalStoreTO employees(List<EmployeeTO> employees) {
        this.employees = employees;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof InternalStoreTO)) {
            return false;
        }
        if (!o.equals(this)) {
            return false;
        }
        InternalStoreTO internalStoreTO = (InternalStoreTO) o;
        return Objects.equals(employees, internalStoreTO.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employees);
    }

    @Override
    public String toString() {
        return "InternalStoreTO{" +
            " employees='" + getEmployees() + "'" +
            "}" + super.toString();
    }    

}
