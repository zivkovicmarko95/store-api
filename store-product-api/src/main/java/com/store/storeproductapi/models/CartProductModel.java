package com.store.storeproductapi.models;

import java.util.Objects;

public class CartProductModel {
    
    private String productId;
    private int selectedQuantity;

    public CartProductModel() {
    }

    public CartProductModel(String productId, int selectedQuantity) {
        this.productId = productId;
        this.selectedQuantity = selectedQuantity;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getSelectedQuantity() {
        return this.selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public CartProductModel productId(String productId) {
        this.productId = productId;
        return this;
    }

    public CartProductModel selectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CartProductModel)) {
            return false;
        }
        CartProductModel cartProductModel = (CartProductModel) o;
        return Objects.equals(productId, cartProductModel.productId) && 
                selectedQuantity == cartProductModel.selectedQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, selectedQuantity);
    }

    @Override
    public String toString() {
        return "{" +
            " productId='" + getProductId() + "'" +
            ", selectedQuantity='" + getSelectedQuantity() + "'" +
            "}";
    }

}
