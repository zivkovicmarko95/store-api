package com.store.storeauthapi.utils;

import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;

// TODO: move this class to shared module
public class JwtUtils {
    
    private JwtUtils() {

    }

    public static AccessToken decode(final String jwtToken) {
        // TODO: add argument verifier
        AccessToken token;
        
        try {
            token = TokenVerifier.create(jwtToken, AccessToken.class)
                .getToken();
        } catch (VerificationException e) {
            throw new RuntimeException("");
        }
        return token;
    }
 
}
