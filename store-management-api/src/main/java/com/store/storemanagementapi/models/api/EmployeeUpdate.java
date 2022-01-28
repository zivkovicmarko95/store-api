package com.store.storemanagementapi.models.api;

import java.util.Objects;

public class EmployeeUpdate {
    
    private String id;
    private String firstname;
    private String lastname;
    private String address;
    private String phonenumber;
    private int salary;

    public EmployeeUpdate() {
    }

    public EmployeeUpdate(String id, String firstname, String lastname, String address, 
            String phonenumber, int salary) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phonenumber = phonenumber;
        this.salary = salary;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public EmployeeUpdate id(String id) {
        this.id = id;
        return this;
    }

    public EmployeeUpdate firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public EmployeeUpdate lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public EmployeeUpdate address(String address) {
        this.address = address;
        return this;
    }

    public EmployeeUpdate phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public EmployeeUpdate salary(int salary) {
        this.salary = salary;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EmployeeUpdate)) {
            return false;
        }
        EmployeeUpdate employeeUpdate = (EmployeeUpdate) o;
        return Objects.equals(id, employeeUpdate.id) && 
                Objects.equals(firstname, employeeUpdate.firstname) && 
                Objects.equals(lastname, employeeUpdate.lastname) && 
                Objects.equals(address, employeeUpdate.address) && 
                Objects.equals(phonenumber, employeeUpdate.phonenumber) && 
                salary == employeeUpdate.salary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, address, phonenumber, salary);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", address='" + getAddress() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", salary='" + getSalary() + "'" +
            "}";
    }

}
