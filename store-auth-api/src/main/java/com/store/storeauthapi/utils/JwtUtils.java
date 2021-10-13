package com.store.storeauthapi.utils;

import com.store.storeauthapi.exceptions.StoreVerificationException;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;

// TODO: move this class to shared module
public class JwtUtils {
    
    private JwtUtils() {

    }

    public static AccessToken decode(final String jwtToken) {
        ArgumentVerifier.verifyNotNull(jwtToken);

        AccessToken token;
        
        try {
            token = TokenVerifier.create(jwtToken, AccessToken.class)
                .getToken();
        } catch (VerificationException e) {
            throw new StoreVerificationException(
                "JWT token is not valid."
            );
        }

        return token;
    }
 
}
