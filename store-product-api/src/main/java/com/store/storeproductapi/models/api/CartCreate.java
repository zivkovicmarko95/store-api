package com.store.storeproductapi.models.api;

import java.util.Objects;

public class CartCreate {
    
    private String productId; 
    private String accountId; 
    private int quantity;

    public CartCreate() {
    }

    public CartCreate(String productId, String accountId, int quantity) {
        this.productId = productId;
        this.accountId = accountId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartCreate productId(String productId) {
        this.productId = productId;
        return this;
    }

    public CartCreate accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public CartCreate quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CartCreate)) {
            return false;
        }
        CartCreate cartCreate = (CartCreate) o;
        return Objects.equals(productId, cartCreate.productId) && 
                Objects.equals(accountId, cartCreate.accountId) && 
                quantity == cartCreate.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, accountId, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " productId='" + getProductId() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }


}
