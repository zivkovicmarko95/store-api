package com.store.storeanalyticsapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.store.storeanalyticsapi.exceptions.ResourceNotFoundException;
import com.store.storeanalyticsapi.models.AnalyticsModel;
import com.store.storeanalyticsapi.models.AnalyticsProduct;
import com.store.storeanalyticsapi.repositories.AnalyticsRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class AnalyticsServiceTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private AnalyticsRepository analyticsRepository;

    private AnalyticsService analyticsService;

    @BeforeEach
    void setup() {
        analyticsService = new AnalyticsService(analyticsRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.analyticsRepository);
    }

    @Test
    void findById() {

        final AnalyticsModel analitycsModel = PODAM_FACTORY.manufacturePojo(AnalyticsModel.class);
        final String id = analitycsModel.getId();

        when(this.analyticsRepository.findById(id)).thenReturn(Optional.of(analitycsModel));

        final AnalyticsModel result = this.analyticsService.findById(id);

        verifyAnalitycs(result, analitycsModel);

        verify(this.analyticsRepository).findById(id);
    }

    @Test
    void findById_analitycsNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.analyticsRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.analyticsService.findById(id))
            .isExactlyInstanceOf(ResourceNotFoundException.class)
            .hasNoCause();

        verify(this.analyticsRepository).findById(id);
    }

    @Test
    void findByAccountId() {

        final AnalyticsModel analitycsModel = PODAM_FACTORY.manufacturePojo(AnalyticsModel.class);
        final String accountId = analitycsModel.getAccountId();

        when(this.analyticsRepository.findByAccountId(accountId)).thenReturn(Optional.of(analitycsModel));

        final AnalyticsModel result = this.analyticsService.findByAccountId(accountId);

        verifyAnalitycs(result, analitycsModel);

        verify(this.analyticsRepository).findByAccountId(accountId);
    }

    @Test
    void findByAccountId_analitycsNotFound() {

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.analyticsRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.analyticsService.findByAccountId(accountId))
            .isExactlyInstanceOf(ResourceNotFoundException.class)
            .hasNoCause();

        verify(this.analyticsRepository).findByAccountId(accountId);
    }

    @Test
    void findAll() {

        final List<AnalyticsModel> analyticsModels = PODAM_FACTORY.manufacturePojo(List.class, AnalyticsModel.class);

        when(this.analyticsRepository.findAll()).thenReturn(analyticsModels);

        final List<AnalyticsModel> result = this.analyticsService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSameSizeAs(analyticsModels);
        assertThat(result).containsAll(analyticsModels);

        verify(this.analyticsRepository).findAll();
    }

    @Test
    void createOrUpdateAnalytics_createAnalytics() {

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final Set<AnalyticsProduct> products = PODAM_FACTORY.manufacturePojo(Set.class, AnalyticsProduct.class);
        final AnalyticsModel analitycsModel = new AnalyticsModel(accountId, products);

        when(this.analyticsRepository.findByAccountId(accountId)).thenReturn(Optional.empty());
        when(this.analyticsRepository.save(any())).thenReturn(analitycsModel);

        final AnalyticsModel result = this.analyticsService.createOrUpdateAnalytics(accountId, products);

        verifyAnalitycs(result, analitycsModel);

        verify(this.analyticsRepository).findByAccountId(accountId);
        verify(this.analyticsRepository).save(any());

    }

    @Test
    void createOrUpdateAnalytics_updateAnalitycs() {

        final AnalyticsModel analitycsModel = PODAM_FACTORY.manufacturePojo(AnalyticsModel.class);
        final Set<AnalyticsProduct> products = PODAM_FACTORY.manufacturePojo(Set.class, AnalyticsProduct.class);
        final String accountId = analitycsModel.getAccountId();

        when(this.analyticsRepository.findByAccountId(accountId)).thenReturn(Optional.of(analitycsModel));
        when(this.analyticsRepository.save(analitycsModel)).thenReturn(analitycsModel);

        final AnalyticsModel result = this.analyticsService.createOrUpdateAnalytics(accountId, products);

        verifyAnalitycs(result, analitycsModel);
        assertThat(result.getProducts()).containsAll(products);

        verify(this.analyticsRepository).findByAccountId(accountId);
        verify(this.analyticsRepository).save(analitycsModel);
    }

    @Test
    void removeById() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        this.analyticsService.removeById(id);

        verify(this.analyticsRepository).deleteById(id);
    }

    private void verifyAnalitycs(final AnalyticsModel result, final AnalyticsModel analitycsModel) {
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(analitycsModel.getId());
        assertThat(result.getAccountId()).isEqualTo(analitycsModel.getAccountId());
        assertThat(result.getCreatedDate()).isEqualTo(analitycsModel.getCreatedDate());
        assertThat(result.getProducts()).containsAll(analitycsModel.getProducts());

    }

}
