package com.store.storemanagementapi.models;

import java.util.Date;
import java.util.Objects;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class EmployeeModel {
    
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String address;
    @Indexed(unique = true)
    private String phoneNumber;
    private int salary;
    private EmployeeStatusEnum status;
    private Date startedWorkingDate;
    private Date endOfWorkingDate;

    public EmployeeModel() {
    }

    public EmployeeModel(final String firstname, final String lastname, final String address, final String phoneNumber, final int salary, 
            final EmployeeStatusEnum status) {
        ArgumentVerifier.verifyNotNull(firstname, lastname, address, phoneNumber, status);
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.status = status;
        this.startedWorkingDate = new Date();;
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

    public EmployeeStatusEnum getStatus() {
        return this.status;
    }

    public void setStatus(EmployeeStatusEnum status) {
        this.status = status;
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

    public EmployeeModel status(EmployeeStatusEnum status) {
        this.status = status;
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
                Objects.equals(status, employeeModel.status) && 
                Objects.equals(startedWorkingDate, employeeModel.startedWorkingDate) && 
                Objects.equals(endOfWorkingDate, employeeModel.endOfWorkingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, address, phoneNumber, salary, status, startedWorkingDate, endOfWorkingDate);
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
            ", status='" + getStatus() + "'" +
            ", startedWorkingDate='" + getStartedWorkingDate() + "'" +
            ", endOfWorkingDate='" + getEndOfWorkingDate() + "'" +
            "}";
    }

}
