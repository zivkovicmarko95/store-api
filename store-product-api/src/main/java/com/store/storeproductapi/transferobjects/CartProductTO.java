package com.store.storeproductapi.transferobjects;

import java.util.Objects;

public class CartProductTO {
    
    private ProductTO product;
    private int selectedQuantity;

    public ProductTO getProduct() {
        return this.product;
    }

    public void setProduct(ProductTO product) {
        this.product = product;
    }

    public int getSelectedQuantity() {
        return this.selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public CartProductTO product(ProductTO product) {
        this.product = product;
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
        return Objects.equals(product, cartProductTO.product) && 
                selectedQuantity == cartProductTO.selectedQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, selectedQuantity);
    }

    @Override
    public String toString() {
        return "{" +
            " product='" + getProduct() + "'" +
            ", selectedQuantity='" + getSelectedQuantity() + "'" +
            "}";
    }

}
