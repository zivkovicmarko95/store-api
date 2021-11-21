package com.store.storeproductapi.models.api;

import java.util.Objects;

public class Cart {
    
    private String cartId; 
    private String productId; 
    // TODO: check how can I check the accountId
    private String accountId; 
    private int quantity;

    public Cart() {
    }

    public Cart(String cartId, String productId, String accountId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.accountId = accountId;
        this.quantity = quantity;
    }

    public String getCartId() {
        return this.cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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

    public Cart cartId(String cartId) {
        this.cartId = cartId;
        return this;
    }

    public Cart productId(String productId) {
        this.productId = productId;
        return this;
    }

    public Cart accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public Cart quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cart)) {
            return false;
        }
        Cart cart = (Cart) o;
        return Objects.equals(cartId, cart.cartId) && 
                Objects.equals(productId, cart.productId) && 
                Objects.equals(accountId, cart.accountId) && 
                quantity == cart.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId, accountId, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " cartId='" + getCartId() + "'" +
            ", productId='" + getProductId() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }

}
