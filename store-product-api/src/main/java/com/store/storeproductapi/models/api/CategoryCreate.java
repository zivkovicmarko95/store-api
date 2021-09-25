package com.store.storeproductapi.models.api;

import java.util.Objects;

public class CategoryCreate {
    
    private String title; 
    private String description;

    public CategoryCreate() {
    }

    public CategoryCreate(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryCreate title(String title) {
        this.title = title;
        return this;
    }

    public CategoryCreate description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CategoryCreate)) {
            return false;
        }
        CategoryCreate categoryCreate = (CategoryCreate) o;
        return Objects.equals(title, categoryCreate.title) && 
                Objects.equals(description, categoryCreate.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

}
