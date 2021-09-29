package com.store.storeauthapi.services;

import java.util.Map;

public interface LoginService {
    
    /**
     * Method used for login User
     * 
     * @param username provided username
     * @param password provided password
     * @return map of provided parameters by keycloak
     */
    Map<String, ?> login(final String username, final String password);
    
}
