package com.store.storemanagementapi.transferobjects;

import java.util.Objects;

import com.store.storesharedmodule.utils.ArgumentVerifier;

public class StoreTO {
    
    private String id;
    private String city;
    private String street;
    private String streetNumber;
    private String phoneNumber;
    private String zipCode;
    private String storeStatus;

    public StoreTO() {

    }

    public StoreTO(String id, String city, String street, String streetNumber, String phoneNumber, 
            String zipCode, String storeStatus) {
        ArgumentVerifier.verifyNotNull(id, city, street, streetNumber, phoneNumber, zipCode, storeStatus);
        this.id = id;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.storeStatus = storeStatus;
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

    public String getStoreStatus() {
        return this.storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public StoreTO id(String id) {
        this.id = id;
        return this;
    }

    public StoreTO city(String city) {
        this.city = city;
        return this;
    }

    public StoreTO street(String street) {
        this.street = street;
        return this;
    }

    public StoreTO streetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public StoreTO phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public StoreTO zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public StoreTO storeStatus(String storeStatus) {
        this.storeStatus = storeStatus;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StoreTO)) {
            return false;
        }
        StoreTO storeTO = (StoreTO) o;
        return Objects.equals(id, storeTO.id) && 
                Objects.equals(city, storeTO.city) && 
                Objects.equals(street, storeTO.street) && 
                Objects.equals(streetNumber, storeTO.streetNumber) && 
                Objects.equals(phoneNumber, storeTO.phoneNumber) && 
                Objects.equals(zipCode, storeTO.zipCode) && 
                Objects.equals(storeStatus, storeTO.storeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street, streetNumber, phoneNumber, zipCode, storeStatus);
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
            "}";
    }

}
