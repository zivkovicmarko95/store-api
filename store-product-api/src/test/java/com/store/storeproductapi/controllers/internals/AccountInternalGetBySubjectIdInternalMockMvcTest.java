package com.store.storeproductapi.controllers.internals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.AccountControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.transferobjects.AccountTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration( classes = {
    GlobalExceptionHandler.class, InternalAccountController.class
} )
class AccountInternalGetBySubjectIdInternalMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountControllerHelper accountControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(accountControllerHelper);
    }

    @Test
    void internalAccountsAccountSubjectIdGet() throws Exception {

        final AccountTO accountTO = PODAM_FACTORY.manufacturePojo(AccountTO.class);
        final String subjectId = accountTO.getSubjectId();

        when(this.accountControllerHelper.internalAccountsAccountSubjectIdGet(subjectId))
                .thenReturn(accountTO);

        this.mockMvc.perform(get(ApiTestConstants.INTERNAL_ACCOUNTS_WITH_ID, subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(accountTO.getId()))
                .andExpect(jsonPath("subjectId").value(accountTO.getSubjectId()))
                .andExpect(jsonPath("username").value(accountTO.getUsername()));

        verify(this.accountControllerHelper).internalAccountsAccountSubjectIdGet(subjectId);
    }

}
