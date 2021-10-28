package com.store.storeproductapi.controllers.internals;

import com.store.storeproductapi.controllers.helpers.AccountControllerHelper;
import com.store.storeproductapi.models.api.AccountCreate;
import com.store.storeproductapi.transferobjects.AccountTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/accounts")
public class InternalAccountController {
    
    private final AccountControllerHelper accountControllerHelper;

    @Autowired
    public InternalAccountController(AccountControllerHelper accountControllerHelper) {
        this.accountControllerHelper = accountControllerHelper;
    }

    @PostMapping
    public ResponseEntity<AccountTO> internalAccountsPost(@RequestBody final AccountCreate accountCreate) {
        
        return new ResponseEntity<>(
                accountControllerHelper.internalAccountsPost(accountCreate),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<AccountTO> internalAccountsAccountSubjectIdGet(@PathVariable final String subjectId) {

        return new ResponseEntity<>(
                accountControllerHelper.internalAccountsAccountSubjectIdGet(subjectId),
                HttpStatus.OK
        );
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<DeleteResultTO> internalAccountsAccountIdDelete(@PathVariable final String accountId) {
        
        return new ResponseEntity<>(
            accountControllerHelper.internalAccountsAccountIdDelete(accountId),
            HttpStatus.NO_CONTENT
            );
    }

    @DeleteMapping("/{accountId}/inactive")
    public ResponseEntity<AccountTO> internalAccountsAccountIdInactiveDelete(@PathVariable final String accountId) {

        return new ResponseEntity<>(
                accountControllerHelper.internalAccountsAccountIdInactiveDelete(accountId),
                HttpStatus.ACCEPTED
        );
    }

}
