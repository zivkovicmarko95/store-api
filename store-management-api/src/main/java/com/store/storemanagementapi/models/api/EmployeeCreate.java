package com.store.storemanagementapi.models.api;

import java.util.Objects;

public class EmployeeCreate {
    
    private String firstname;
    private String lastname;
    private String address;
    private String phoneNumber;
    private int salary;

    public EmployeeCreate() {
    }

    public EmployeeCreate(String firstname, String lastname, String address, 
            String phoneNumber, int salary) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
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

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public EmployeeCreate firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public EmployeeCreate lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public EmployeeCreate address(String address) {
        this.address = address;
        return this;
    }

    public EmployeeCreate phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public EmployeeCreate salary(int salary) {
        this.salary = salary;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EmployeeCreate)) {
            return false;
        }
        EmployeeCreate employeeCreate = (EmployeeCreate) o;
        return Objects.equals(firstname, employeeCreate.firstname) && 
                Objects.equals(lastname, employeeCreate.lastname) && 
                Objects.equals(address, employeeCreate.address) && 
                Objects.equals(phoneNumber, employeeCreate.phoneNumber) && 
                salary == employeeCreate.salary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, address, phoneNumber, salary);
    }

    @Override
    public String toString() {
        return "{" +
            " firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", salary='" + getSalary() + "'" +
            "}";
    }

}
