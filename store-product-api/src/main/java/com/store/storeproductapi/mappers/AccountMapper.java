package com.store.storeproductapi.mappers;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.transferobjects.AccountTO;

public class AccountMapper {
    
    private AccountMapper() {

    }

    public static AccountTO mapRepoToAccountTO(final AccountModel account) {

        return new AccountTO()
                .id(account.getId())
                .subjectId(account.getSubjectId())
                .username(account.getUsername())
                .loginDate(account.getLoginDate())
                .createdDate(account.getCreatedDate());
    }

}
