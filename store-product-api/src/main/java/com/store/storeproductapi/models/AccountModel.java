package com.store.storeproductapi.models;

import java.util.Date;
import java.util.Objects;

import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class AccountModel {
    
    @Id
    private String id;
    private String subjectId;
    private String cartId;
    private String username;
    private Date loginDate;
    private Date createdDate;
    // if account is not active for a year, delete it
    private boolean isActive;

    public AccountModel() {
    }

    public AccountModel(String subjectId, String cartId, String username) {
        ArgumentVerifier.verifyNotNull(subjectId, username);
        this.subjectId = subjectId;
        this.cartId = cartId;
        this.username = username;
        this.loginDate = new Date();
        this.createdDate = new Date();
        this.isActive = true;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCartId() {
        return this.cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isIsActive() {
        return this.isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public AccountModel id(String id) {
        this.id = id;
        return this;
    }

    public AccountModel subjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public AccountModel cartId(String cartId) {
        this.cartId = cartId;
        return this;
    }

    public AccountModel username(String username) {
        this.username = username;
        return this;
    }

    public AccountModel loginDate(Date loginDate) {
        this.loginDate = loginDate;
        return this;
    }

    public AccountModel createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public AccountModel isActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AccountModel)) {
            return false;
        }
        AccountModel accountModel = (AccountModel) o;
        return Objects.equals(id, accountModel.id) && 
                Objects.equals(subjectId, accountModel.subjectId) && 
                Objects.equals(cartId, accountModel.cartId) && 
                Objects.equals(username, accountModel.username) && 
                Objects.equals(loginDate, accountModel.loginDate) && 
                Objects.equals(createdDate, accountModel.createdDate) && 
                isActive == accountModel.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, cartId, username, loginDate, createdDate, isActive);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", subjectId='" + getSubjectId() + "'" +
            ", cartId='" + getCartId() + "'" +
            ", username='" + getUsername() + "'" +
            ", loginDate='" + getLoginDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }    

}
