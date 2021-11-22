package com.store.storemanagementapi.models;

import java.util.Objects;
import java.util.Set;

import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StoreModel {
    
    @Id
    private String id;
    private String city;
    private String street;
    private String streetNumber;
    private String phoneNumber;
    private String zipCode;
    private StoreStatusEnum storeStatus;
    private Set<String> employeeIds;

    public StoreModel() {
    }

    public StoreModel(String city, String street, String streetNumber, String phoneNumber, String zipCode, 
            StoreStatusEnum storeStatus, Set<String> employeeIds) {
        ArgumentVerifier.verifyNotNull(city, street, streetNumber, phoneNumber, zipCode, storeStatus, employeeIds);
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.storeStatus = storeStatus;
        this.employeeIds = employeeIds;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public StoreStatusEnum getStoreStatus() {
        return this.storeStatus;
    }

    public void setStoreStatus(StoreStatusEnum storeStatus) {
        this.storeStatus = storeStatus;
    }

    public Set<String> getEmployeeIds() {
        return this.employeeIds;
    }

    public void setEmployeeIds(Set<String> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public StoreModel id(String id) {
        this.id = id;
        return this;
    }

    public StoreModel city(String city) {
        this.city = city;
        return this;
    }

    public StoreModel street(String street) {
        this.street = street;
        return this;
    }

    public StoreModel streetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public StoreModel phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public StoreModel zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public StoreModel storeStatus(StoreStatusEnum storeStatus) {
        this.storeStatus = storeStatus;
        return this;
    }

    public StoreModel employeeIds(Set<String> employeeIds) {
        this.employeeIds = employeeIds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StoreModel)) {
            return false;
        }
        StoreModel storeModel = (StoreModel) o;
        return Objects.equals(id, storeModel.id) && 
                Objects.equals(city, storeModel.city) && 
                Objects.equals(street, storeModel.street) && 
                Objects.equals(streetNumber, storeModel.streetNumber) && 
                Objects.equals(phoneNumber, storeModel.phoneNumber) && 
                Objects.equals(zipCode, storeModel.zipCode) && 
                Objects.equals(storeStatus, storeModel.storeStatus) && 
                Objects.equals(employeeIds, storeModel.employeeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street, streetNumber, phoneNumber, zipCode, storeStatus, employeeIds);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", city='" + getCity() + "'" +
            ", street='" + getStreet() + "'" +
            ", streetNumber='" + getStreetNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", storeStatus='" + getStoreStatus() + "'" +
            ", employeeIds='" + getEmployeeIds() + "'" +
            "}";
    }

}
