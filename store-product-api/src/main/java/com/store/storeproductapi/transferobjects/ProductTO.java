package com.store.storeproductapi.transferobjects;

import java.util.Objects;

public class ProductTO {
    
    private String id;
    private String title;
    private float price;
    private String description;
    private String imgUrl;
    private int quantity;
    private float avgUserRating;
    private int numberOfVotes;

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

    public float getAvgUserRating() {
        return this.avgUserRating;
    }

    public void setAvgUserRating(float avgUserRating) {
        this.avgUserRating = avgUserRating;
    }

    public int getNumberOfVotes() {
        return this.numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public ProductTO id(String id) {
        this.id = id;
        return this;
    }

    public ProductTO title(String title) {
        this.title = title;
        return this;
    }

    public ProductTO price(float price) {
        this.price = price;
        return this;
    }

    public ProductTO description(String description) {
        this.description = description;
        return this;
    }

    public ProductTO imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public ProductTO quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductTO avgUserRating(float avgUserRating) {
        this.avgUserRating = avgUserRating;
        return this;
    }

    public ProductTO numberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductTO)) {
            return false;
        }
        ProductTO productTO = (ProductTO) o;
        return Objects.equals(id, productTO.id) && 
                Objects.equals(title, productTO.title) && 
                price == productTO.price && 
                Objects.equals(description, productTO.description) && 
                Objects.equals(imgUrl, productTO.imgUrl) && 
                quantity == productTO.quantity && 
                avgUserRating == productTO.avgUserRating && 
                numberOfVotes == productTO.numberOfVotes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, description, imgUrl, quantity, avgUserRating, numberOfVotes);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", price='" + getPrice() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", avgUserRating='" + getAvgUserRating() + "'" +
            ", numberOfVotes='" + getNumberOfVotes() + "'" +
            "}";
    }

}
