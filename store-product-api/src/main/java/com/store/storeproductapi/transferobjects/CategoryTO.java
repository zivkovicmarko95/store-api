package com.store.storeproductapi.transferobjects;

import java.util.Objects;
import java.util.Set;

public class CategoryTO {
    
    private String id;
    private String title;
    private String description;
    private Set<ProductTO> products;

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

    public Set<ProductTO> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductTO> products) {
        this.products = products;
    }

    public CategoryTO id(String id) {
        this.id = id;
        return this;
    }

    public CategoryTO title(String title) {
        this.title = title;
        return this;
    }

    public CategoryTO description(String description) {
        this.description = description;
        return this;
    }

    public CategoryTO products(Set<ProductTO> products) {
        this.products = products;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CategoryTO)) {
            return false;
        }
        CategoryTO categoryTO = (CategoryTO) o;
        return Objects.equals(id, categoryTO.id) && 
                Objects.equals(title, categoryTO.title) && 
                Objects.equals(description, categoryTO.description) && 
                Objects.equals(products, categoryTO.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, products);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", products='" + getProducts() + "'" +
            "}";
    }
    
}
