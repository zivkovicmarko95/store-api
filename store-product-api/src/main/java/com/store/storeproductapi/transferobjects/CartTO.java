package com.store.storeproductapi.transferobjects;

import java.util.Objects;
import java.util.Set;

public class CartTO {
    
    private String id;
    private Set<CartProductTO> cartProducts;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<CartProductTO> getCartProducts() {
        return this.cartProducts;
    }

    public void setCartProducts(Set<CartProductTO> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public CartTO id(String id) {
        this.id = id;
        return this;
    }

    public CartTO cartProducts(Set<CartProductTO> cartProducts) {
        this.cartProducts = cartProducts;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CartTO)) {
            return false;
        }
        CartTO cartTO = (CartTO) o;
        return Objects.equals(id, cartTO.id) && 
                Objects.equals(cartProducts, cartTO.cartProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cartProducts);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", cartProducts='" + getCartProducts() + "'" +
            "}";
    }

}
