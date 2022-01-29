package com.store.storeproductapi.configurations;

import com.store.storeproductapi.utils.KeycloakRoleConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final String jwkSetUri = "http://localhost:8080/auth/realms/store-api/protocol/openid-connect/certs";
    private final String[] permitUrls = {  "/api/categories/**", "/api/products/**", "/v2/api-docs", "/swagger-resources/**", 
            "/configuration/ui", "/swagger-ui/**", "/actuator/health/**" };
    private final String internalEndpoints = "/api/internal/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> {
            try {
                authorize.antMatchers(permitUrls).permitAll()
                    .antMatchers(internalEndpoints).hasRole("admin")
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer()
                    .jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Necessary to decode JWT token to get roles
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

}
