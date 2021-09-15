package com.store.storeproductapi.models;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ProductModel {

    @Id
    private String id;

    private String title;
    private float price;
    private String description;
    private String imgUrl;
    private int quantity;
    private float avgUserRating;
    private int numberOfVotes;
    private boolean visible;

    public ProductModel() {
    }

    public ProductModel(String id, String title, float price, String description, String imgUrl, int quantity,
            float avgUserRating, int numberOfVotes, boolean visible) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.quantity = quantity;
        this.avgUserRating = avgUserRating;
        this.numberOfVotes = numberOfVotes;
        this.visible = visible;
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

    public boolean isVisible() {
        return this.visible;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ProductModel id(String id) {
        this.id = id;
        return this;
    }

    public ProductModel title(String title) {
        this.title = title;
        return this;
    }

    public ProductModel price(float price) {
        this.price = price;
        return this;
    }

    public ProductModel description(String description) {
        this.description = description;
        return this;
    }

    public ProductModel imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public ProductModel quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductModel avgUserRating(float avgUserRating) {
        this.avgUserRating = avgUserRating;
        return this;
    }

    public ProductModel numberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
        return this;
    }

    public ProductModel visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductModel)) {
            return false;
        }
        ProductModel product = (ProductModel) o;
        return Objects.equals(id, product.id) && Objects.equals(title, product.title) && price == product.price
                && Objects.equals(description, product.description) && Objects.equals(imgUrl, product.imgUrl)
                && quantity == product.quantity && avgUserRating == product.avgUserRating
                && numberOfVotes == product.numberOfVotes && visible == product.visible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, description, imgUrl, quantity, avgUserRating, numberOfVotes, visible);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", title='" + getTitle() + "'" + ", price='" + getPrice() + "'"
                + ", description='" + getDescription() + "'" + ", imgUrl='" + getImgUrl() + "'" + ", quantity='"
                + getQuantity() + "'" + ", avgUserRating='" + getAvgUserRating() + "'" + ", numberOfVotes='"
                + getNumberOfVotes() + "'" + ", visible='" + isVisible() + "'" + "}";
    }

}
