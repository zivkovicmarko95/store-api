package com.store.storeproductapi.controllers.internals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.AccountControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.models.api.AccountCreate;
import com.store.storeproductapi.transferobjects.AccountTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, InternalAccountController.class
})
class AccountInternalCreateMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountControllerHelper accountControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(accountControllerHelper);
    }

    @Test
    void internalAccountsPost() throws Exception {

        final AccountCreate accountCreate = PODAM_FACTORY.manufacturePojo(AccountCreate.class);
        final AccountTO accountTO = PODAM_FACTORY.manufacturePojo(AccountTO.class)
                .subjectId(accountCreate.getSubjectId())
                .username(accountCreate.getUsername());

        when(this.accountControllerHelper.internalAccountsPost(accountCreate))
                .thenReturn(accountTO);

        this.mockMvc.perform(post(ApiTestConstants.INTERNAL_ACCOUNTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(accountTO.getId()))
                .andExpect(jsonPath("subjectId").value(accountTO.getSubjectId()))
                .andExpect(jsonPath("username").value(accountTO.getUsername()));

        verify(this.accountControllerHelper).internalAccountsPost(accountCreate);
    }

    @Test
    void internalAccountsPost_missingRequestBody() throws Exception {

        this.mockMvc.perform(post(ApiTestConstants.INTERNAL_ACCOUNTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
