package com.store.storeproductapi.controllers;

import com.store.storeproductapi.controllers.helpers.AccountControllerHelper;
import com.store.storeproductapi.transferobjects.AccountTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountControllerHelper accountControllerHelper;

    @Autowired
    public AccountController(AccountControllerHelper accountControllerHelper) {
        this.accountControllerHelper = accountControllerHelper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountTO> accountsAccountIdGet(final String id) {
        
        return new ResponseEntity<>(
                accountControllerHelper.accountsAccountIdGet(id),
                HttpStatus.OK
        );
    }

}
