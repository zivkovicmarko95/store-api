package com.store.storeproductapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.transferobjects.AccountTO;
import com.store.storesharedmodule.models.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = StoreProductApiApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc( addFilters = false )
class AccountIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String USERNAME = "preferred_username";
    private static final String SUBJECT_ID = "sub";

    private static final AccountModel ACCOUNT_MODEL = PODAM_FACTORY.manufacturePojo(AccountModel.class);
            
    public static final String ACCOUNT_USERNAME = ACCOUNT_MODEL.getUsername();
    public static final String ACCOUNT_SUBJECT_ID = ACCOUNT_MODEL.getSubjectId();

    private static final Set<GrantedAuthority> STORE_AUTHORITIES = Set.of(
            new SimpleGrantedAuthority("ROLE_admin"),
            new SimpleGrantedAuthority("ROLE_user")
    );

    private static final String ACCOUNT_ID = ACCOUNT_MODEL.getId();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        final Map<String, Object> userDetails = Map.of(USERNAME, ACCOUNT_USERNAME, SUBJECT_ID, ACCOUNT_SUBJECT_ID);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(generateJwtToken(userDetails), STORE_AUTHORITIES));

        this.accountRepository.save(ACCOUNT_MODEL);
    }

    @AfterEach
    void after() {
        SecurityContextHolder.clearContext();
        this.accountRepository.delete(ACCOUNT_MODEL);
    }

    @Test
    void accountsAccountIdGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ACCOUNTS_WITH_ID, ACCOUNT_ID))
                .andExpect(status().isOk());

        final AccountTO resultAccount = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountTO.class);

        assertThat(resultAccount).isNotNull();
        assertThat(resultAccount.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(resultAccount.getSubjectId()).isEqualTo(ACCOUNT_MODEL.getSubjectId());
        assertThat(resultAccount.getUsername()).isEqualTo(ACCOUNT_MODEL.getUsername());
        assertThat(resultAccount.getCreatedDate()).isEqualTo(ACCOUNT_MODEL.getCreatedDate());
        assertThat(resultAccount.getLoginDate()).isEqualTo(ACCOUNT_MODEL.getLoginDate());
    }

    @Test
    void accountsAccountIdGet_accountNotFound() throws Exception {

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ACCOUNTS_WITH_ID, accountId))
                .andExpect(status().isUnauthorized());

        final HttpResponse httpResponse = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(httpResponse.getMessage())
            .isEqualTo(String.format("Not authorized to access this resource.", accountId));
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
