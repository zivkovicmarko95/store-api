package com.store.storeproductapi.its.config;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@Order(-1)
public class HttpAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final String USERNAME = "preferred_username";
    private static final String SUBJECT_ID = "sub";
    
    public static final String ACCOUNT_USERNAME = PODAM_FACTORY.manufacturePojo(String.class);
    public static final String ACCOUNT_SUBJECT_ID = PODAM_FACTORY.manufacturePojo(String.class);

    private static final Set<GrantedAuthority> STORE_AUTHORITIES = Set.of(
            new SimpleGrantedAuthority("ROLE_admin"),
            new SimpleGrantedAuthority("ROLE_user")
    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .and().addFilterAfter(new FilterChainProxy() {

                @Override
                public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                        throws IOException, ServletException {
                    
                    final Map<String, Object> userDetails = Map.of(USERNAME, ACCOUNT_USERNAME, SUBJECT_ID, ACCOUNT_SUBJECT_ID);
                    final Jwt jwt = generateJwtToken(userDetails);

                    SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, STORE_AUTHORITIES));

                    chain.doFilter(request, response);
                }

            }, BasicAuthenticationFilter.class);
    }

    private Jwt generateJwtToken(final Map<String, Object> userDetails) {
        return new Jwt(
            PODAM_FACTORY.manufacturePojo(String.class), 
            new Date().toInstant(),
            new Date().toInstant().plusSeconds(100),
            PODAM_FACTORY.manufacturePojo(HashMap.class, String.class, Object.class),
            userDetails);
    }

}
