package com.store.storemanagementapi.transferobjects;

import java.util.Date;
import java.util.Objects;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;

public class EmployeeTO {
    
    private String id;
    private String firstname;
    private String lastname;
    private String address;
    private String phoneNumber;
    private int salary;
    private EmployeeStatusEnum employeeStatusEnum;
    private Date startedWorkingDate;
    private Date endOfWorkingDate;

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

    public EmployeeStatusEnum getEmployeeStatusEnum() {
        return this.employeeStatusEnum;
    }

    public void setEmployeeStatusEnum(EmployeeStatusEnum employeeStatusEnum) {
        this.employeeStatusEnum = employeeStatusEnum;
    }

    public Date getStartedWorkingDate() {
        return this.startedWorkingDate;
    }

    public void setStartedWorkingDate(Date startedWorkingDate) {
        this.startedWorkingDate = startedWorkingDate;
    }

    public Date getEndOfWorkingDate() {
        return this.endOfWorkingDate;
    }

    public void setEndOfWorkingDate(Date endOfWorkingDate) {
        this.endOfWorkingDate = endOfWorkingDate;
    }

    public EmployeeTO id(String id) {
        this.id = id;
        return this;
    }

    public EmployeeTO firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public EmployeeTO lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public EmployeeTO address(String address) {
        this.address = address;
        return this;
    }

    public EmployeeTO phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public EmployeeTO salary(int salary) {
        this.salary = salary;
        return this;
    }

    public EmployeeTO employeeStatusEnum(EmployeeStatusEnum employeeStatusEnum) {
        this.employeeStatusEnum = employeeStatusEnum;
        return this;
    }

    public EmployeeTO startedWorkingDate(Date startedWorkingDate) {
        this.startedWorkingDate = startedWorkingDate;
        return this;
    }

    public EmployeeTO endOfWorkingDate(Date endOfWorkingDate) {
        this.endOfWorkingDate = endOfWorkingDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EmployeeTO)) {
            return false;
        }
        EmployeeTO employeeTO = (EmployeeTO) o;
        return Objects.equals(id, employeeTO.id) && 
                Objects.equals(firstname, employeeTO.firstname) && 
                Objects.equals(lastname, employeeTO.lastname) && 
                Objects.equals(address, employeeTO.address) && 
                Objects.equals(phoneNumber, employeeTO.phoneNumber) && 
                salary == employeeTO.salary && 
                Objects.equals(employeeStatusEnum, employeeTO.employeeStatusEnum) && 
                Objects.equals(startedWorkingDate, employeeTO.startedWorkingDate) && 
                Objects.equals(endOfWorkingDate, employeeTO.endOfWorkingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, address, phoneNumber, salary, 
                employeeStatusEnum, startedWorkingDate, endOfWorkingDate);
    }

    @Override
    public String toString() {
        return "EmployeeTO{" +
            " id='" + getId() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", salary='" + getSalary() + "'" +
            ", employeeStatusEnum='" + getEmployeeStatusEnum() + "'" +
            ", startedWorkingDate='" + getStartedWorkingDate() + "'" +
            ", endOfWorkingDate='" + getEndOfWorkingDate() + "'" +
            "}";
    }

}
