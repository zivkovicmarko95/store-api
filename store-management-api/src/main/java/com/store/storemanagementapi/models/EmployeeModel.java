package com.store.storemanagementapi.models;

import java.util.Date;
import java.util.Objects;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class EmployeeModel {
    
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String address;
    private String phoneNumber;
    private int salary;
    private EmployeeStatusEnum employeeStatusEnum;
    private Date startedWorkingDate;
    private Date endOfWorkingDate;

    public EmployeeModel() {
    }

    public EmployeeModel(String firstname, String lastname, String address, String phoneNumber, 
            int salary, EmployeeStatusEnum employeeStatusEnum) {
        ArgumentVerifier.verifyNotNull(firstname, lastname, address, phoneNumber, employeeStatusEnum);
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.employeeStatusEnum = employeeStatusEnum;
        this.startedWorkingDate = new Date();
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

    public EmployeeModel id(String id) {
        this.id = id;
        return this;
    }

    public EmployeeModel firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public EmployeeModel lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public EmployeeModel address(String address) {
        this.address = address;
        return this;
    }

    public EmployeeModel phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public EmployeeModel salary(int salary) {
        this.salary = salary;
        return this;
    }

    public EmployeeModel employeeStatusEnum(EmployeeStatusEnum employeeStatusEnum) {
        this.employeeStatusEnum = employeeStatusEnum;
        return this;
    }

    public EmployeeModel startedWorkingDate(Date startedWorkingDate) {
        this.startedWorkingDate = startedWorkingDate;
        return this;
    }

    public EmployeeModel endOfWorkingDate(Date endOfWorkingDate) {
        this.endOfWorkingDate = endOfWorkingDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EmployeeModel)) {
            return false;
        }
        EmployeeModel employeeModel = (EmployeeModel) o;
        return Objects.equals(id, employeeModel.id) && 
                Objects.equals(firstname, employeeModel.firstname) && 
                Objects.equals(lastname, employeeModel.lastname) && 
                Objects.equals(address, employeeModel.address) && 
                Objects.equals(phoneNumber, employeeModel.phoneNumber) && 
                salary == employeeModel.salary && 
                Objects.equals(employeeStatusEnum, employeeModel.employeeStatusEnum) && 
                Objects.equals(startedWorkingDate, employeeModel.startedWorkingDate) && 
                Objects.equals(endOfWorkingDate, employeeModel.endOfWorkingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, address, phoneNumber, salary, 
                employeeStatusEnum, startedWorkingDate, endOfWorkingDate);
    }

    @Override
    public String toString() {
        return "{" +
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
