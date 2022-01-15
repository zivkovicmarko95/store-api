package com.store.storemanagementapi.transferobjects;

import java.util.Objects;
import java.util.Set;

public class InternalStoreTO extends StoreTO {
    
    private Set<EmployeeTO> employees;

    public Set<EmployeeTO> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<EmployeeTO> employees) {
        this.employees = employees;
    }

    public InternalStoreTO employees(Set<EmployeeTO> employees) {
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
