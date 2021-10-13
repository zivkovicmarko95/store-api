package com.store.storesharedmodule.models;

import java.util.Date;
import java.util.Objects;

public class UserEntity {
    
    private String subjectId;
    private String username;
    private Date loginDate;

    private UserEntity() {
    }

    public UserEntity(String subjectId, String username) {
        this.subjectId = subjectId;
        this.username = username;
        this.loginDate = new Date();
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

    public UserEntity subjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public UserEntity username(String username) {
        this.username = username;
        return this;
    }

    public UserEntity loginDate(Date loginDate) {
        this.loginDate = loginDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserEntity)) {
            return false;
        }
        UserEntity userEvent = (UserEntity) o;
        return Objects.equals(subjectId, userEvent.subjectId) && 
                Objects.equals(username, userEvent.username) && 
                Objects.equals(loginDate, userEvent.loginDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, username, loginDate);
    }

    @Override
    public String toString() {
        return "{" +
            " subjectId='" + getSubjectId() + "'" +
            ", username='" + getUsername() + "'" +
            ", loginDate='" + getLoginDate() + "'" +
            "}";
    }

}
