package com.store.storeauthapi.models;

import java.util.Objects;

public class LoginCredentials {
    
    private String username;
    private String password;

    public LoginCredentials() {
    }

    public LoginCredentials(String username, String password) {
        // TODO: add argument verifier - verify not null
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginCredentials username(String username) {
        this.username = username;
        return this;
    }

    public LoginCredentials password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LoginCredentials)) {
            return false;
        }
        LoginCredentials loginCredentials = (LoginCredentials) o;
        return Objects.equals(username, loginCredentials.username) && 
                Objects.equals(password, loginCredentials.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }

}
