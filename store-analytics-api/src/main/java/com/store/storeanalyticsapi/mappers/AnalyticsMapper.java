package com.store.storeanalyticsapi.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeanalyticsapi.models.AnalyticsModel;
import com.store.storeanalyticsapi.models.AnalyticsProduct;
import com.store.storeanalyticsapi.transferobjects.AnalyticsProductTO;
import com.store.storeanalyticsapi.transferobjects.AnalyticsTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

public class AnalyticsMapper {
    
    private AnalyticsMapper() {
        
    }

    public static List<AnalyticsTO> mapReposToAnalyticsTOs(final List<AnalyticsModel> analytics) {
        ArgumentVerifier.verifyNotNull(analytics);

        return analytics.stream()
                .map(AnalyticsMapper::mapRepoToAnalyticsTO)
                .collect(Collectors.toList());
    }

    public static AnalyticsTO mapRepoToAnalyticsTO(final AnalyticsModel analyticsModel) {
        ArgumentVerifier.verifyNotNull(analyticsModel);

        return new AnalyticsTO()
                .id(analyticsModel.getId())
                .accountId(analyticsModel.getAccountId())
                .createdDate(analyticsModel.getCreatedDate())
                .products(mapRepoAnalyticsProductsToAnalyticsProductTOs(analyticsModel.getProducts()));
    }

    private static Set<AnalyticsProductTO> mapRepoAnalyticsProductsToAnalyticsProductTOs(final Set<AnalyticsProduct> products) {
        return products.stream()
                .map(AnalyticsMapper::mapRepoAnalyticsProductToAnalyticsProductTO)
                .collect(Collectors.toSet());
    }

    private static AnalyticsProductTO mapRepoAnalyticsProductToAnalyticsProductTO(final AnalyticsProduct analyticsProduct) {

        return new AnalyticsProductTO()
                .id(analyticsProduct.getId())
                .isBought(analyticsProduct.getIsBought())
                .views(analyticsProduct.getViews());
    }

}
