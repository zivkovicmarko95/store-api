package com.store.storeproductapi.services.impl;

import java.util.Date;
import java.util.Optional;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.services.AccountService;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountModel findById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return accountRepository.findById(id)
                .orElseThrow(() -> 
                    new StoreResourceNotFoundException(
                        String.format(
                            "Account with provided id %s is not found",
                            id
                        )
                    )
                );
    }

    @Override
    public AccountModel findBySubjectId(String subjectId) {
        ArgumentVerifier.verifyNotNull(subjectId);

        return accountRepository.findBySubjectId(subjectId)
                .orElseThrow(() -> 
                    new StoreGeneralException(
                        String.format(
                            "Account with provided subject ID %s is not found",
                            subjectId
                        )
                    )
                );
    }

    @Override
    public Optional<AccountModel> findByUsername(final String username) {
        ArgumentVerifier.verifyNotNull(username);

        return accountRepository.findByUsername(username);
    }

    @Override
    public AccountModel save(String subjectId, String username) {
        ArgumentVerifier.verifyNotNull(subjectId, username);
        
        final Optional<AccountModel> foundAccount = findByUsername(username);

        if (foundAccount.isPresent()) {
            throw new ResourceStateException(
                String.format(
                    "Account with id %s exists in the database.",
                    username
                )
            );
        }

        // cart is not provided in that case
        final AccountModel account = new AccountModel(subjectId, null, username);

        return accountRepository.insert(account);
    }

    @Override
    public AccountModel updateLoginDate(String id, Date loginDate) {
        ArgumentVerifier.verifyNotNull(id, loginDate);

        final AccountModel account = findById(id);
        account.setLoginDate(loginDate);

        return accountRepository.save(account);
    }

    @Override
    public AccountModel inactiveAccount(String id) {
        ArgumentVerifier.verifyNotNull(id);

        final AccountModel account = findById(id);

        if (!account.getIsActive()) {
            throw new ResourceStateException(
                String.format(
                    "Account is already set to be inactive. Account: %s",
                    account.toString()
                )
            );
        }

        account.isActive(false);

        return accountRepository.save(account);
    }

    @Override
    public void removeAccount(String id) {
        ArgumentVerifier.verifyNotNull(id);

        LOGGER.info("Removing account with ID: {}", id);

        accountRepository.deleteById(id);

        LOGGER.warn("Account has been removed");
    }

    @Override
    public AccountModel assignCartToAccount(String accountId, String cartId) {
        ArgumentVerifier.verifyNotNull(accountId, cartId);

        final AccountModel account = findById(accountId);
        account.setCartId(cartId);
        LOGGER.info("Assigning cartId {} to the account with ID {}", cartId, accountId);

        return accountRepository.save(account);
    }

    @Override
    public AccountModel unassignCartFromAccount(String accountId) {
        ArgumentVerifier.verifyNotNull(accountId);

        final AccountModel account = findById(accountId);
        account.setCartId(null);
        LOGGER.info("Unassigning cart from account with ID {}", accountId);

        return accountRepository.save(account);
    }
    
}
