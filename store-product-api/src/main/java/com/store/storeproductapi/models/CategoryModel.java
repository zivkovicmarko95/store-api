package com.store.storeproductapi.models;

import java.util.Objects;
import java.util.Set;

import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class CategoryModel {

    @Id
    private String id;
    private String title;
    private String description;
    private Set<String> productIds;
    private boolean visible;

    public CategoryModel() {
    }

    public CategoryModel(String title, String description, Set<String> productIds, boolean visible) {
        ArgumentVerifier.verifyNotNull(title, description);
        this.title = title;
        this.description = description;
        this.productIds = productIds;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getProductIds() {
        return this.productIds;
    }

    public void setProductIds(Set<String> productIds) {
        this.productIds = productIds;
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

    public CategoryModel id(String id) {
        this.id = id;
        return this;
    }

    public CategoryModel title(String title) {
        this.title = title;
        return this;
    }

    public CategoryModel description(String description) {
        this.description = description;
        return this;
    }

    public CategoryModel productIds(Set<String> productIds) {
        this.productIds = productIds;
        return this;
    }

    public CategoryModel visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CategoryModel)) {
            return false;
        }
        CategoryModel category = (CategoryModel) o;
        return Objects.equals(id, category.id) && Objects.equals(title, category.title)
                && Objects.equals(description, category.description) && Objects.equals(productIds, category.productIds)
                && visible == category.visible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, productIds, visible);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", title='" + getTitle() + "'" + ", description='" + getDescription()
                + "'" + ", productIds='" + getProductIds() + "'" + ", visible='" + isVisible() + "'" + "}";
    }

}
