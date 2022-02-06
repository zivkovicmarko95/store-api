package com.store.storeorderapi.transferobjects;

import java.util.Objects;

public class OrderedProductTO {
    
    private String id;
    private int quantity;
    private double price;
    private double discount;

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

    public OrderedProductTO id(String id) {
        this.id = id;
        return this;
    }

    public OrderedProductTO quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderedProductTO price(double price) {
        this.price = price;
        return this;
    }

    public OrderedProductTO discount(double discount) {
        this.discount = discount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderedProductTO)) {
            return false;
        }
        OrderedProductTO orderProductTO = (OrderedProductTO) o;
        return Objects.equals(id, orderProductTO.id) && 
                quantity == orderProductTO.quantity && 
                price == orderProductTO.price && 
                discount == orderProductTO.discount;
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
