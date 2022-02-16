package com.store.storeanalyticsapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.store.storeanalyticsapi.models.AnalyticsModel;
import com.store.storeanalyticsapi.models.AnalyticsProduct;
import com.store.storeanalyticsapi.transferobjects.AnalyticsTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class AnalyticsMapperTest {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapReposToAnalyticsTOs() {

        final List<AnalyticsModel> analyticsModels = PODAM_FACTORY.manufacturePojo(List.class, AnalyticsModel.class);

        final List<AnalyticsTO> result = AnalyticsMapper.mapReposToAnalyticsTOs(analyticsModels);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        result.forEach(analyticsTO -> {

            final AnalyticsModel analytics = analyticsModels.stream()
                .filter(analyticsModel -> analyticsModel.getId().equals(analyticsTO.getId()))
                .findFirst().get();

            verifyAnalytics(analyticsTO, analytics);
        });
    }

    @Test
    void mapRepoToAnalyticsTO() {

        final AnalyticsModel analyticsModel = PODAM_FACTORY.manufacturePojo(AnalyticsModel.class);

        final AnalyticsTO result = AnalyticsMapper.mapRepoToAnalyticsTO(analyticsModel);

        verifyAnalytics(result, analyticsModel);
    }

    private void verifyAnalytics(final AnalyticsTO result, final AnalyticsModel analyticsModel) {

        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(analyticsModel.getAccountId());
        assertThat(result.getId()).isEqualTo(analyticsModel.getId());
        assertThat(result.getCreatedDate()).isEqualTo(analyticsModel.getCreatedDate());

        result.getProducts().forEach(resultProduct -> {

            final AnalyticsProduct analyticsProduct = analyticsModel.getProducts().stream()
                    .filter(product -> product.getId().equals(resultProduct.getId())) 
                    .findFirst()
                    .get();

            assertThat(resultProduct.getId()).isEqualTo(analyticsProduct.getId());
            assertThat(resultProduct.getIsBought()).isEqualTo(analyticsProduct.getIsBought());
            assertThat(resultProduct.getViews()).isEqualTo(analyticsProduct.getViews());
        });
    }
    
}
