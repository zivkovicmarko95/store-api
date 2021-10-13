package com.store.storeproductapi.transferobjects;

import java.util.Date;
import java.util.Objects;

public class AccountTO {
    
    private String id;
    private String subjectId;
    private String username;
    private Date loginDate;
    private Date createdDate;

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

    public AccountTO id(String id) {
        this.id = id;
        return this;
    }

    public AccountTO subjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public AccountTO username(String username) {
        this.username = username;
        return this;
    }

    public AccountTO loginDate(Date loginDate) {
        this.loginDate = loginDate;
        return this;
    }

    public AccountTO createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AccountTO)) {
            return false;
        }
        AccountTO accountTO = (AccountTO) o;
        return Objects.equals(id, accountTO.id) && 
                Objects.equals(subjectId, accountTO.subjectId) && 
                Objects.equals(username, accountTO.username) && 
                Objects.equals(loginDate, accountTO.loginDate) && 
                Objects.equals(createdDate, accountTO.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, username, loginDate, createdDate);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", subjectId='" + getSubjectId() + "'" +
            ", username='" + getUsername() + "'" +
            ", loginDate='" + getLoginDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }

}
