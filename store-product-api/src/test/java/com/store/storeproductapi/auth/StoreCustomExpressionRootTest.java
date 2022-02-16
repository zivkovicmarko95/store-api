package com.store.storeproductapi.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.repositories.AccountRepository;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class })
@Import(StoreCustomExpressionRootTest.TestConfig.class)
class StoreCustomExpressionRootTest {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final String USERNAME = "preferred_username";
    private static final String SUBJECT_ID = "sub";
    
    private static final AccountModel ACCOUNT_MODEL = PODAM_FACTORY.manufacturePojo(AccountModel.class);
    private static final String ACCOUNT_ID = ACCOUNT_MODEL.getId();
    private static final String ACCOUNT_USERNAME = ACCOUNT_MODEL.getUsername();
    private static final String ACCOUNT_SUBJECT_ID = ACCOUNT_MODEL.getSubjectId();

    private static final Set<GrantedAuthority> STORE_AUTHORITIES = Set.of(
            new SimpleGrantedAuthority("ROLE_admin"),
            new SimpleGrantedAuthority("ROLE_user")
    );

    public static class TestConfig {

        @Bean
        public JwtAuthenticationToken jwtAuthenticationToken() {

            final Map<String, Object> claims = Map.of(USERNAME, ACCOUNT_USERNAME, SUBJECT_ID, ACCOUNT_SUBJECT_ID);
            
            final Jwt jwt = new Jwt(
                    "tra.la.la.la",
                    new Date().toInstant(),
                    new Date().toInstant().plusSeconds(100),
                    Maps.newHashMap("alg", "RS256"),
                    claims
            );

            final Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_admin"), new SimpleGrantedAuthority("ROLE_user"));
            SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, STORE_AUTHORITIES));

            return new JwtAuthenticationToken(jwt, authorities);
        }

        @Bean
        public StoreCustomExpressionRoot storeCustomExpressionRoot(final Authentication authentication) {
            return new StoreCustomExpressionRoot(authentication);
        }
    }

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private StoreCustomExpressionRoot storeCustomExpressionRoot;

    @AfterEach
    private void after() {
        verifyNoMoreInteractions(this.accountRepository);   
    }

    @Test
    void ownsAccount() {

        when(this.accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(ACCOUNT_MODEL));
        when(this.accountRepository.findBySubjectId(ACCOUNT_SUBJECT_ID)).thenReturn(Optional.of(ACCOUNT_MODEL));

        final boolean result = this.storeCustomExpressionRoot.ownsAccount(ACCOUNT_ID);

        assertThat(result).isTrue();

        verify(this.accountRepository).findById(ACCOUNT_ID);
        verify(this.accountRepository).findBySubjectId(ACCOUNT_SUBJECT_ID);
    }

    @Test
    void ownsAccount_accountIdAndUsernameNotMatch() {

    }
    
}
