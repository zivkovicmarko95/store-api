package com.store.storeauthapi.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.store.storeauthapi.proxy.AuthTokenServiceProxy;
import com.store.storeauthapi.services.LoginService;
import com.store.storeauthapi.utils.JwtUtils;
import com.store.storesharedmodule.constants.EventsExchange;
import com.store.storesharedmodule.constants.EventsRoutingKeys;
import com.store.storesharedmodule.models.UserEntity;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.keycloak.representations.AccessToken;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ACCESS_TOKEN = "access_token";

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final AuthTokenServiceProxy authTokenServiceProxy;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public LoginServiceImpl(AuthTokenServiceProxy authTokenServiceProxy, AmqpTemplate amqpTemplate) {
        this.authTokenServiceProxy = authTokenServiceProxy;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public Map<String, ?> login(String username, String password) {
        ArgumentVerifier.verifyNotNull(username, password);

        Map<String, Object> form = new HashMap<>();
        final String grantType = "password";

        form.put(GRANT_TYPE, grantType);
        form.put(CLIENT_ID, clientId);
        form.put(CLIENT_SECRET, clientSecret);
        form.put(USERNAME, username);
        form.put(PASSWORD, password);

        final Map<String, ?> loginResponse = authTokenServiceProxy.login(form);
        final String jwtToken = (String) loginResponse.get(ACCESS_TOKEN);
        final AccessToken accessToken = JwtUtils.decode(jwtToken);
        
        final String subject = accessToken.getSubject();
        final UserEntity userEvent = new UserEntity(subject, username);

        amqpTemplate.convertAndSend(EventsExchange.GLOBAL_EXCHANGE.getKey(), EventsRoutingKeys.AUTH_LOGIN_ROUTING_KEY.getKey(), userEvent);

        return loginResponse;
    }

}
