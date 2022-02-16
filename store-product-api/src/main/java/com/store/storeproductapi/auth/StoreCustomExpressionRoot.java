package com.store.storeproductapi.auth;

import java.util.Optional;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.utils.AuthUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

public class StoreCustomExpressionRoot extends CustomMethodSecurityExpressionRoot {

    @Autowired
    private AccountRepository accountRepository;

    public StoreCustomExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean ownsAccount(final String id) {

        final Optional<AccountModel> optionalAccount = this.accountRepository.findById(id);
        final String subjectId = AuthUtils.getSubjectIdFromJWT();

        if (optionalAccount.isEmpty()) {
            return false;
        }
        
        return this.accountRepository.findBySubjectId(subjectId)
                .equals(optionalAccount);
    }
    
}
