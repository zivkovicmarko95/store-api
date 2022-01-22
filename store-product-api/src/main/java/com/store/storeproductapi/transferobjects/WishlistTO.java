package com.store.storeproductapi.transferobjects;

import java.util.Objects;
import java.util.Set;

public class WishlistTO {
    
    private String id;
    private Set<ProductTO> products;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<ProductTO> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductTO> products) {
        this.products = products;
    }

    public WishlistTO id(String id) {
        this.id = id;
        return this;
    }

    public WishlistTO products(Set<ProductTO> products) {
        this.products = products;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WishlistTO)) {
            return false;
        }
        WishlistTO wishlistTO = (WishlistTO) o;
        return Objects.equals(id, wishlistTO.id) && 
                Objects.equals(products, wishlistTO.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", products='" + getProducts() + "'" +
            "}";
    }
    
}
