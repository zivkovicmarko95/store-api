package com.store.storeproductapi.models;

import java.util.Objects;
import java.util.Set;

import com.store.storeproductapi.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CartModel {
    
    @Id
    private String id;
    private String accountId;
    private Set<CartProductModel> cartProducts;

    public CartModel() {
    }

    public CartModel(String accountId, Set<CartProductModel> cartProducts) {
        ArgumentVerifier.verifyNotNull(accountId);
        this.accountId = accountId;
        this.cartProducts = cartProducts;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Set<CartProductModel> getCartProducts() {
        return this.cartProducts;
    }

    public void setCartProducts(Set<CartProductModel> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public CartModel id(String id) {
        this.id = id;
        return this;
    }

    public CartModel accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public CartModel cartProducts(Set<CartProductModel> cartProducts) {
        this.cartProducts = cartProducts;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CartModel)) {
            return false;
        }
        CartModel cartModel = (CartModel) o;
        return Objects.equals(id, cartModel.id) && 
                Objects.equals(accountId, cartModel.accountId) && 
                Objects.equals(cartProducts, cartModel.cartProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, cartProducts);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", cartProducts='" + getCartProducts() + "'" +
            "}";
    }

}
