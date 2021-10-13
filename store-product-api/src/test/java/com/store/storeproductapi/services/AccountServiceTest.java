package com.store.storeproductapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.services.impl.AccountServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ MockitoExtension.class, SpringExtension.class })
class AccountServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void findById() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));

        final AccountModel result = this.accountService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCartId()).isEqualTo(account.getCartId());
        assertThat(result.getCreatedDate()).isEqualTo(account.getCreatedDate());
        assertThat(result.getIsActive()).isEqualTo(account.getIsActive());
        assertThat(result.getLoginDate()).isEqualTo(account.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(account.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(account.getUsername());
        assertThat(result).isEqualTo(account);

        verify(this.accountRepository).findById(id);
    }

    @Test
    void findById_accountNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findById(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Account with provided id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.accountRepository).findById(id);
    }

    @Test
    void findBySubjectId() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String subjectId = account.getSubjectId();

        when(this.accountRepository.findBySubjectId(subjectId)).thenReturn(Optional.of(account));

        final AccountModel result = this.accountService.findBySubjectId(subjectId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCartId()).isEqualTo(account.getCartId());
        assertThat(result.getCreatedDate()).isEqualTo(account.getCreatedDate());
        assertThat(result.getIsActive()).isEqualTo(account.getIsActive());
        assertThat(result.getLoginDate()).isEqualTo(account.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(account.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(account.getUsername());
        assertThat(result).isEqualTo(account);

        verify(this.accountRepository).findBySubjectId(subjectId);
    }

    @Test
    void findBySubjectId_accountNotFound() {

        final String subjectId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.accountRepository.findBySubjectId(subjectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findBySubjectId(subjectId))
            .isExactlyInstanceOf(StoreGeneralException.class)
            .hasMessageStartingWith(
                String.format(
                    "Account with provided subject ID %s is not found",
                    subjectId
                )
            )
            .hasNoCause();

        verify(this.accountRepository).findBySubjectId(subjectId);
    }

    @Test
    void findByUsername() {

        final AccountModel accountModel = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String username = accountModel.getUsername();

        when(this.accountRepository.findByUsername(username)).thenReturn(Optional.of(accountModel));

        final Optional<AccountModel> optionalAccount = this.accountService.findByUsername(username);

        assertThat(optionalAccount).isNotEmpty();
        assertThat(optionalAccount).contains(accountModel);

        verify(this.accountRepository).findByUsername(username);
    }

    @Test
    void findByUsername_accountNotFound() {

        final String username = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        final Optional<AccountModel> optionalAccount = this.accountService.findByUsername(username);

        assertThat(optionalAccount).isEmpty();

        verify(this.accountRepository).findByUsername(username);
    }

    @Test
    void save() {

        final String username = PODAM_FACTORY.manufacturePojo(String.class);
        final String subjectId = PODAM_FACTORY.manufacturePojo(String.class);
        final AccountModel account = new AccountModel(subjectId, null, username);

        when(this.accountRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(this.accountRepository.insert(any(AccountModel.class))).thenReturn(account);

        final AccountModel result = this.accountService.save(subjectId, username);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(account);

        verify(this.accountRepository).findByUsername(username);
        verify(this.accountRepository).insert(any(AccountModel.class));
    }

    @Test
    void save_accountAlreadyExists() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String username = account.getUsername();
        final String subjectId = account.getSubjectId();

        when(this.accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> this.accountService.save(subjectId, username))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Account with id %s exists in the database.",
                    username
                )
            )
            .hasNoCause();

        verify(this.accountRepository).findByUsername(username);
    }

    @Test
    void updateLoginDate() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();
        final Date loginDate = new Date();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(this.accountRepository.save(account)).thenReturn(account);

        final AccountModel result = this.accountService.updateLoginDate(id, loginDate);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(account);
        assertThat(result.getLoginDate()).isEqualTo(loginDate);

        verify(this.accountRepository).findById(id);
        verify(this.accountRepository).save(account);
    }

    @Test
    void updateLoginDate_accountNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final Date loginDate = new Date();

        when(this.accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.accountService.updateLoginDate(id, loginDate))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Account with provided id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.accountRepository).findById(id);
    }

    @Test
    void inactiveAccount() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(this.accountRepository.save(account)).thenReturn(account);

        final AccountModel result = this.accountService.inactiveAccount(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.isIsActive()).isFalse();

        verify(this.accountRepository).findById(id);
        verify(this.accountRepository).save(account);
    }

    @Test
    void inactiveAccount_accountNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.accountService.inactiveAccount(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasNoCause();

        verify(this.accountRepository).findById(id);
    }

    @Test
    void inactiveAccount_accountAlreadyInactive() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class)
                .isActive(false);
        final String id = account.getId();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> this.accountService.inactiveAccount(id))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasNoCause();

        verify(this.accountRepository).findById(id);
    }

    @Test
    void removeAccount() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        this.accountService.removeAccount(id);

        verify(this.accountRepository).deleteById(id);
    }

    @Test
    void assignCartToAccount() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();
        final String cartId = account.getCartId();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(this.accountRepository.save(account)).thenReturn(account);

        final AccountModel result = this.accountService.assignCartToAccount(id, cartId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCartId()).isEqualTo(account.getCartId());
        assertThat(result).isEqualTo(account);

        verify(this.accountRepository).findById(id);
        verify(this.accountRepository).save(account);
    }

    @Test
    void assignCartToAccount_accountNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String cartId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.accountService.assignCartToAccount(id, cartId))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasNoCause();

        verify(this.accountRepository).findById(id);
    }

    @Test
    void unassignCartFromAccount() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final String id = account.getId();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(this.accountRepository.save(account)).thenReturn(account);

        final AccountModel result = this.accountService.unassignCartFromAccount(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCartId()).isNull();
        assertThat(result).isEqualTo(account);

        verify(this.accountRepository).findById(id);
        verify(this.accountRepository).save(account);
    }

    @Test
    void unassignCartFromAccount_accountNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.accountService.unassignCartFromAccount(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasNoCause();

        verify(this.accountRepository).findById(id);
    }

}
