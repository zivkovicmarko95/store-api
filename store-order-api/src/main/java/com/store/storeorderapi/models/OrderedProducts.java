package com.store.storeorderapi.models;

import java.util.Objects;

import com.store.storesharedmodule.utils.ArgumentVerifier;

public class OrderedProducts {
    
    private String id;
    private int quantity;
    private double price;
    private double discount;

    public OrderedProducts() {
    }

    public OrderedProducts(String id, int quantity, double price, double discount) {
        ArgumentVerifier.verifyNotNull(id);
        
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public OrderedProducts id(String id) {
        this.id = id;
        return this;
    }

    public OrderedProducts quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderedProducts price(double price) {
        this.price = price;
        return this;
    }

    public OrderedProducts discount(double discount) {
        this.discount = discount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderedProducts)) {
            return false;
        }
        OrderedProducts orderedProducts = (OrderedProducts) o;
        return Objects.equals(id, orderedProducts.id) && 
                quantity == orderedProducts.quantity && 
                price == orderedProducts.price && 
                discount == orderedProducts.discount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, price, discount);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", price='" + getPrice() + "'" +
            ", discount='" + getDiscount() + "'" +
            "}";
    }

}
