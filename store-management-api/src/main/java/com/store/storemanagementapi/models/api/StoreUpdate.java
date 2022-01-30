package com.store.storemanagementapi.models.api;

import java.util.Objects;

public class StoreUpdate {
    
    private String city;
    private String street;
    private String streetNumber;
    private String phoneNumber;
    private String zipcode;
    private String status;

    public StoreUpdate() {
    }

    public StoreUpdate(String city, String street, String streetNumber, 
            String phoneNumber, String zipcode, String status) {
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.status = status;
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

    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StoreUpdate city(String city) {
        this.city = city;
        return this;
    }

    public StoreUpdate street(String street) {
        this.street = street;
        return this;
    }

    public StoreUpdate streetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public StoreUpdate phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public StoreUpdate zipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public StoreUpdate status(String status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StoreUpdate)) {
            return false;
        }
        StoreUpdate storeUpdate = (StoreUpdate) o;
        return  Objects.equals(city, storeUpdate.city) && 
                Objects.equals(street, storeUpdate.street) && 
                Objects.equals(streetNumber, storeUpdate.streetNumber) && 
                Objects.equals(phoneNumber, storeUpdate.phoneNumber) && 
                Objects.equals(zipcode, storeUpdate.zipcode) && 
                Objects.equals(status, storeUpdate.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, streetNumber, phoneNumber, zipcode, status);
    }

    @Override
    public String toString() {
        return "{" +
            "city='" + getCity() + "'" +
            ", street='" + getStreet() + "'" +
            ", streetNumber='" + getStreetNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }

}
