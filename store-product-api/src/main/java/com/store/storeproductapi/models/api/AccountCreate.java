package com.store.storeproductapi.models.api;

import java.util.Objects;

public class AccountCreate {
    
    private String subjectId;
    private String username;

    public AccountCreate() {
    }

    public AccountCreate(String subjectId, String username) {
        this.subjectId = subjectId;
        this.username = username;
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

    public AccountCreate subjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public AccountCreate username(String username) {
        this.username = username;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AccountCreate)) {
            return false;
        }
        AccountCreate accountCreate = (AccountCreate) o;
        return Objects.equals(subjectId, accountCreate.subjectId) && 
                Objects.equals(username, accountCreate.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, username);
    }

    @Override
    public String toString() {
        return "{" +
            " subjectId='" + getSubjectId() + "'" +
            ", username='" + getUsername() + "'" +
            "}";
    }


}
