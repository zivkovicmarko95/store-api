package com.store.storeapigateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    
    private final String jwkSetUri = "http://localhost:8080/auth/realms/store-api";

    private final String[] PUBLIC_URLS = {
        "/api/auth/login",
        "/api/products",
        "/api/categories",
        "/api/stores"
    };

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        
        http.authorizeExchange()
            .pathMatchers(PUBLIC_URLS)
            .permitAll()
            .anyExchange()
            .authenticated().and().oauth2ResourceServer().jwt();

        http.csrf().disable();

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = (NimbusReactiveJwtDecoder) ReactiveJwtDecoders.fromIssuerLocation(jwkSetUri);
        
        return jwtDecoder;
    }

}
