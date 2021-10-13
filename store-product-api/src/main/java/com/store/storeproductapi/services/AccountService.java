package com.store.storeproductapi.services;

import java.util.Date;
import java.util.Optional;

import com.store.storeproductapi.models.AccountModel;

public interface AccountService {
    
    /**
     * Method that returns account by ID
     * 
     * @param id ID of targeted account
     * @return found {@link AccountModel}
     */
    AccountModel findById(final String id);

    /**
     * Method that returns account by subject ID
     * 
     * @param subjectId subject ID of targeted account
     * @return found {@link AccountModel}
     */
    AccountModel findBySubjectId(final String subjectId);

    /**
     * Method that returns optional account by username
     * 
     * @param username username of targeted account
     * @return found {@link AccountModel}
     */
    Optional<AccountModel> findByUsername(final String username);

    /**
     * Method that saves account to the database
     * 
     * @param subjectId ID of a subject
     * @param username username of targeted account
     * @return created account {@link AccountModel}
     */
    AccountModel save(final String subjectId, final String username);

    /**
     * Method that updates login date of targeted account
     * 
     * @param id ID of account
     * @param loginDate login date of targeted account
     * @return updated {@link AccountModel}
     */
    AccountModel updateLoginDate(final String id, final Date loginDate);

    /**
     * Method that sets account to be inactive
     * 
     * @param id ID of targeted account
     * @return update {@link AccountModel}
     */
    AccountModel inactiveAccount(final String id);

    /**
     * Method that removes an account. Account needs to be inactive if 
     * admin wants to remove account
     * 
     * @param id ID of targeted account
     */
    void removeAccount(final String id);

    /**
     * Method that returns updated account
     * 
     * @param accountId ID of targeted Account
     * @param cartId ID of the cart
     * @return updated account {@link AccountModel}
     */
    AccountModel assignCartToAccount(String accountId, String cartId);

    /**
     * Method that returns updated account
     * 
     * @param accountId ID of targeted Account
     * @return updated account {@link AccountModel}
     */
    AccountModel unassignCartFromAccount(String accountId);

}
