package com.store.storeanalyticsapi.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.store.storeanalyticsapi.exceptions.ResourceNotFoundException;
import com.store.storeanalyticsapi.models.AnalyticsModel;
import com.store.storeanalyticsapi.models.AnalyticsProduct;
import com.store.storeanalyticsapi.repositories.AnalyticsRepository;
import com.store.storesharedmodule.annotations.StoreMongoTransaction;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsService.class);
    
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(final AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    /**
     * Find {@link AnalyticsModel} by provided analitycs ID
     * 
     * @param id ID of the analitycs
     * @throws ResourceNotFoundException if not found
     * @return found {@link AnalyticsModel}
     */
    public AnalyticsModel findById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return analyticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Analitycs with ID %s is not found.", id)
                ));
    }


    /**
     * Return all analytics saved in the database
     * 
     * @return List of all {@link AnalyticsModel}
     */
    public List<AnalyticsModel> findAll() {
        return analyticsRepository.findAll();
    }

    /**
     * Find {@link AnalyticsModel} by provided Account ID
     * 
     * @param accountId ID of the account
     * @throws ResourceNotFoundException if not found
     * @return found {@link AnalyticsModel}
     */
    public AnalyticsModel findByAccountId(final String accountId) {
        ArgumentVerifier.verifyNotNull(accountId);

        return analyticsRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Provided ID %s of the account is not found.", accountId)
                ));
    }

    /**
     * Save or update analitycs if exists
     * 
     * @param accountId ID of the account 
     * @param products  Products that are viewed/bought by the client
     * @return created {@link AnalyticsModel}
     */
    @StoreMongoTransaction
    public AnalyticsModel createOrUpdateAnalytics(final String accountId, final Set<AnalyticsProduct> products) {
        ArgumentVerifier.verifyNotNull(accountId);
        ArgumentVerifier.verifyNotEmpty(products);

        final Optional<AnalyticsModel> optionalAnalytics = this.analyticsRepository.findByAccountId(accountId);

        if (optionalAnalytics.isPresent()) {

            LOGGER.info("Updating account ");

            final AnalyticsModel analitycsModel = optionalAnalytics.get();
            
            products.forEach(requestedProduct -> {

                analitycsModel.getProducts().stream()
                        .filter(product -> product.getId().equals(requestedProduct.getId()))
                        .findFirst()
                        .ifPresentOrElse(product -> {
                            // update number of views
                            product.setViews(product.getViews() + 1);
                            // update if product is bought
                            if (!product.getIsBought()) {
                                product.setIsBought(product.getIsBought());
                            }
                        }, () -> {
                            analitycsModel.getProducts().add(requestedProduct);
                        });
            });

            return analyticsRepository.save(analitycsModel);
        }

        LOGGER.info("Creating analytics for account with ID {}.", accountId);

        return analyticsRepository.save(new AnalyticsModel(accountId, products));
    }

    /**
     * Remove analitycs by ID
     * 
     * @param id ID of the analitycs
     */
    @StoreMongoTransaction
    public void removeById(final String id) {

        LOGGER.warn("Removing analitycs with ID {}.", id);
        
        this.analyticsRepository.deleteById(id);
    }

}