package com.store.storeproductapi.its.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.api.AccountCreate;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.transferobjects.AccountTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class InternalAccountIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final AccountModel ACCOUNT_MODEL = PODAM_FACTORY.manufacturePojo(AccountModel.class);
    private static final String ACCOUNT_ID = ACCOUNT_MODEL.getId();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.accountRepository.save(ACCOUNT_MODEL);
    }

    @AfterEach
    void after() {
        this.accountRepository.deleteById(ACCOUNT_ID);
    }

    @Test
    void internalAccountsPost() throws Exception {

        final AccountCreate accountCreate = PODAM_FACTORY.manufacturePojo(AccountCreate.class);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_ACCOUNTS)
                    .content(objectMapper.writeValueAsString(accountCreate))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        final AccountTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(accountCreate.getUsername());
        assertThat(result.getSubjectId()).isEqualTo(accountCreate.getSubjectId());

        this.accountRepository.deleteById(result.getId());
    }

    @Test
    void internalAccountsPost_noRequestBody() throws Exception {

        this.mockMvc.perform(post(ApiTestConstants.INTERNAL_ACCOUNTS)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void internalAccountsAccountSubjectIdGet() throws Exception {

        final String subjectId = ACCOUNT_MODEL.getSubjectId();
        
        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.INTERNAL_ACCOUNTS_WITH_ID, subjectId))
                .andExpect(status().isOk());

        final AccountTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ACCOUNT_MODEL.getId());
        assertThat(result.getCreatedDate()).isEqualTo(ACCOUNT_MODEL.getCreatedDate());
        assertThat(result.getLoginDate()).isEqualTo(ACCOUNT_MODEL.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(ACCOUNT_MODEL.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(ACCOUNT_MODEL.getUsername());
    }

    @Test
    void internalAccountsAccountSubjectIdGet_notFound() throws Exception {

        final String subjectId = PODAM_FACTORY.manufacturePojo(String.class);
        
        this.mockMvc.perform(get(ApiTestConstants.INTERNAL_ACCOUNTS_WITH_ID, subjectId))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void internalAccountsAccountIdDelete() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_ACCOUNTS_WITH_ID, ACCOUNT_ID))
                    .andExpect(status().isNoContent());

        final DeleteResultTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), DeleteResultTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getResourceId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getMessage()).isEqualTo("Account has been removed.");
    }

    @Test
    void internalAccountsAccountIdInactiveDelete() throws Exception {

        this.accountRepository.save(ACCOUNT_MODEL.isActive(true));

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_ACCOUNTS_WITH_ID_INACTIVE, ACCOUNT_ID))
                    .andExpect(status().isAccepted());

        final DeleteResultTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), DeleteResultTO.class);

        assertThat(result).isNotNull();
    }

    @Test
    void internalAccountsAccountIdInactiveDelete_inactiveAccount() throws Exception {

        this.accountRepository.save(ACCOUNT_MODEL.isActive(false));

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_ACCOUNTS_WITH_ID_INACTIVE, ACCOUNT_ID))
                    .andExpect(status().isBadRequest());
    }

}
