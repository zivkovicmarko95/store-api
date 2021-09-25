package com.store.storeproductapi.models.api;

import java.util.Objects;

public class Product {
    
    private String title; 
    private float price; 
    private String description; 
    private String imgUrl; 
    private int quantity;

    public Product() {
    }

    public Product(String title, float price, String description, String imgUrl, int quantity) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.quantity = quantity;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product title(String title) {
        this.title = title;
        return this;
    }

    public Product price(float price) {
        this.price = price;
        return this;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public Product imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public Product quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(title, product.title) && 
                price == product.price && 
                Objects.equals(description, product.description) && 
                Objects.equals(imgUrl, product.imgUrl) && 
                quantity == product.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, description, imgUrl, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            ", price='" + getPrice() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }

}
