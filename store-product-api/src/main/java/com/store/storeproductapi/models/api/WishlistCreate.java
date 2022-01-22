package com.store.storeproductapi.models.api;

import java.util.Objects;

public class WishlistCreate {
    
    private String accountId;
    private String productId;

    public WishlistCreate() {
    }

    public WishlistCreate(String accountId, String productId) {
        this.accountId = accountId;
        this.productId = productId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public WishlistCreate accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public WishlistCreate productId(String productId) {
        this.productId = productId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WishlistCreate)) {
            return false;
        }
        WishlistCreate wishlistCreate = (WishlistCreate) o;
        return Objects.equals(accountId, wishlistCreate.accountId) && 
                Objects.equals(productId, wishlistCreate.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, productId);
    }

    @Override
    public String toString() {
        return "{" +
            " accountId='" + getAccountId() + "'" +
            ", productId='" + getProductId() + "'" +
            "}";
    }    

}
