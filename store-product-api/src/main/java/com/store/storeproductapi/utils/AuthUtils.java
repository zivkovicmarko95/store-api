package com.store.storeproductapi.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtils {
    
    private static final String USERNAME = "preferred_username";
    private static final String SUBJECT_ID = "sub";

    private AuthUtils() {

    }

    public static String getUsernameFromJWT() {
        return getParamFromJWT(USERNAME);
    }

    public static String getSubjectIdFromJWT() {
        return getParamFromJWT(SUBJECT_ID);
    }

    private static String getParamFromJWT(final String param) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            if (authentication.getCredentials() != null && authentication.getCredentials() != "") {
                final Jwt jwt = (Jwt) authentication.getCredentials();
    
                return (String) jwt.getClaims().get(param);
            }
        }

        return null;
    }

}
