package com.store.storeproductapi.controllers.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.api.AccountCreate;
import com.store.storeproductapi.services.AccountService;
import com.store.storeproductapi.transferobjects.AccountTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class AccountControllerHelperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private AccountService accountService;

    private AccountControllerHelper accountControllerHelper;

    @BeforeEach
    void setup() {
        accountControllerHelper = new AccountControllerHelper(accountService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void accountsAccountIdGet() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();

        when(this.accountService.findById(id)).thenReturn(account);

        final AccountTO result = accountControllerHelper.accountsAccountIdGet(id);

        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCreatedDate()).isEqualTo(account.getCreatedDate());
        assertThat(result.getLoginDate()).isEqualTo(account.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(account.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(account.getUsername());
        
        verify(this.accountService).findById(id);
    }

    @Test
    void internalAccountsAccountSubjectIdGet() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String subjectId = account.getSubjectId();

        when(this.accountService.findBySubjectId(subjectId)).thenReturn(account);

        final AccountTO result = accountControllerHelper.internalAccountsAccountSubjectIdGet(subjectId);

        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCreatedDate()).isEqualTo(account.getCreatedDate());
        assertThat(result.getLoginDate()).isEqualTo(account.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(account.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(account.getUsername());
        
        verify(this.accountService).findBySubjectId(subjectId);
    }

    @Test
    void internalAccountsAccountIdInactiveDelete() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();

        when(this.accountService.inactiveAccount(id)).thenReturn(account);

        final AccountTO result = accountControllerHelper.internalAccountsAccountIdInactiveDelete(id);

        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCreatedDate()).isEqualTo(account.getCreatedDate());
        assertThat(result.getLoginDate()).isEqualTo(account.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(account.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(account.getUsername());

        verify(this.accountService).inactiveAccount(id);
    }

    @Test
    void internalAccountsAccountIdDelete() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        final DeleteResultTO result = accountControllerHelper.internalAccountsAccountIdDelete(id);

        assertThat(result.getResourceId()).isEqualTo(id);
        assertThat(result.getMessage()).isEqualTo("Account has been removed.");

        verify(this.accountService).removeAccount(id);
    }

    @Test
    void internalAccountsPost() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final AccountCreate accountCreate = new AccountCreate(account.getSubjectId(), account.getUsername());

        when(this.accountService.save(accountCreate.getSubjectId(), accountCreate.getUsername()))
                .thenReturn(account);

        final AccountTO result = accountControllerHelper.internalAccountsPost(accountCreate);

        assertThat(result.getSubjectId()).isEqualTo(accountCreate.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(accountCreate.getUsername());

        verify(this.accountService).save(accountCreate.getSubjectId(), account.getUsername());
    }

}
