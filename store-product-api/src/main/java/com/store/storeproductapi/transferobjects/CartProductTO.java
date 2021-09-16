package com.store.storeproductapi.transferobjects;

import java.util.Objects;

public class CartProductTO {
    
    private String id;
    private String title;
    private float price;
    private int selectedQuantity;

    public CartProductTO() {
    }

    public CartProductTO(String id, String title, float price, int selectedQuantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.selectedQuantity = selectedQuantity;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSelectedQuantity() {
        return this.selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public CartProductTO id(String id) {
        this.id = id;
        return this;
    }

    public CartProductTO title(String title) {
        this.title = title;
        return this;
    }

    public CartProductTO price(float price) {
        this.price = price;
        return this;
    }

    public CartProductTO selectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CartProductTO)) {
            return false;
        }
        CartProductTO cartProductTO = (CartProductTO) o;
        return Objects.equals(id, cartProductTO.id) && 
                Objects.equals(title, cartProductTO.title) && 
                price == cartProductTO.price && 
                selectedQuantity == cartProductTO.selectedQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, selectedQuantity);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", price='" + getPrice() + "'" +
            ", selectedQuantity='" + getSelectedQuantity() + "'" +
            "}";
    }

}
