package com.store.storeproductapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.transferobjects.AccountTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class AccountMapperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToAccountTO() {

        final AccountModel account = PODAM_FACTORY.manufacturePojo(AccountModel.class);

        final AccountTO result = AccountMapper.mapRepoToAccountTO(account);

        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCreatedDate()).isEqualTo(account.getCreatedDate());
        assertThat(result.getLoginDate()).isEqualTo(account.getLoginDate());
        assertThat(result.getSubjectId()).isEqualTo(account.getSubjectId());
        assertThat(result.getUsername()).isEqualTo(account.getUsername());
    }

}
