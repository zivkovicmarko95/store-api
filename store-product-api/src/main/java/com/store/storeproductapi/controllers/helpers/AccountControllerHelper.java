package com.store.storeproductapi.controllers.helpers;

import com.store.storeproductapi.mappers.AccountMapper;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.api.AccountCreate;
import com.store.storeproductapi.services.AccountService;
import com.store.storeproductapi.transferobjects.AccountTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountControllerHelper {
    
    private final AccountService accountService;

    @Autowired
    public AccountControllerHelper(AccountService accountService) {
        this.accountService = accountService;
    }

    // GET api/accounts/{accountId}
    public AccountTO accountsAccountIdGet(final String accountId) {
        final AccountModel account = this.accountService.findById(accountId);

        return AccountMapper.mapRepoToAccountTO(account);
    }

    // GET api/internal/accounts/{subjectId}
    public AccountTO internalAccountsAccountSubjectIdGet(final String subjectId) {
        final AccountModel account = this.accountService.findBySubjectId(subjectId);

        return AccountMapper.mapRepoToAccountTO(account);
    }

    // DELETE api/internal/accounts/{accountId}/inactive
    public AccountTO internalAccountsAccountIdInactiveDelete(final String accountId) {
        final AccountModel account = this.accountService.inactiveAccount(accountId);

        return AccountMapper.mapRepoToAccountTO(account);
    }

    // DELETE api/internal/accounts/{accountId}
    public DeleteResultTO internalAccountsAccountIdDelete(final String accountId) {
        this.accountService.removeAccount(accountId);

        return new DeleteResultTO()
                .resourceId(accountId)
                .message("Account has been removed.");
    }

    // POST api/internal/accounts
    public AccountTO internalAccountsPost(final AccountCreate accountCreate) {
        final AccountModel account = this.accountService.save(accountCreate.getSubjectId(), accountCreate.getUsername());

        return AccountMapper.mapRepoToAccountTO(account);
    }

}
