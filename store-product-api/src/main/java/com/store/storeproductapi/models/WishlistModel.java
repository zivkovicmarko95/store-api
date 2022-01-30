package com.store.storeproductapi.models;

import java.util.Objects;
import java.util.Set;

import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "wishlists")
public class WishlistModel {
    
    @Id
    private String id;
    private String accountId;
    private Set<String> productIds;

    public WishlistModel() {
    }

    public WishlistModel(final String accountId, final Set<String> productIds) {
        ArgumentVerifier.verifyNotNull(accountId, productIds);
        this.accountId = accountId;
        this.productIds = productIds;
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

    public Set<String> getProductIds() {
        return this.productIds;
    }

    public void setProductIds(Set<String> productIds) {
        this.productIds = productIds;
    }

    public WishlistModel id(String id) {
        this.id = id;
        return this;
    }

    public WishlistModel accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public WishlistModel productIds(Set<String> productIds) {
        this.productIds = productIds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WishlistModel)) {
            return false;
        }
        WishlistModel wishlistModel = (WishlistModel) o;
        return Objects.equals(id, wishlistModel.id) && 
                Objects.equals(accountId, wishlistModel.accountId) && 
                Objects.equals(productIds, wishlistModel.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, productIds);
    }

    @Override
    public String toString() {
        return "WishlistModel {" +
            " id='" + getId() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", productIds='" + getProductIds() + "'" +
            "}";
    }

}
