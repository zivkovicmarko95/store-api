package com.store.storemanagementapi.models.api;

import java.util.Objects;

public class StoreCreate {
    
    private String city;
    private String street;
    private String streetNumber;
    private String phoneNumber;
    private String zipcode;

    public StoreCreate() {
    }

    public StoreCreate(String city, String street, String streetNumber, String phoneNumber, 
            String zipcode) {
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
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

    public StoreCreate city(String city) {
        this.city = city;
        return this;
    }

    public StoreCreate street(String street) {
        this.street = street;
        return this;
    }

    public StoreCreate streetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public StoreCreate phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public StoreCreate zipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StoreCreate)) {
            return false;
        }
        StoreCreate storeCreate = (StoreCreate) o;
        return Objects.equals(city, storeCreate.city) && 
                Objects.equals(street, storeCreate.street) && 
                Objects.equals(streetNumber, storeCreate.streetNumber) && 
                Objects.equals(phoneNumber, storeCreate.phoneNumber) && 
                Objects.equals(zipcode, storeCreate.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, streetNumber, phoneNumber, zipcode);
    }

    @Override
    public String toString() {
        return "{" +
            " city='" + getCity() + "'" +
            ", street='" + getStreet() + "'" +
            ", streetNumber='" + getStreetNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            "}";
    }

}
