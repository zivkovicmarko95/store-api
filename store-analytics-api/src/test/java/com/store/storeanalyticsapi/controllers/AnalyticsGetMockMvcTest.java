package com.store.storeanalyticsapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storeanalyticsapi.constants.ApiTestConstants;
import com.store.storeanalyticsapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeanalyticsapi.mappers.AnalyticsMapper;
import com.store.storeanalyticsapi.models.AnalyticsModel;
import com.store.storeanalyticsapi.services.AnalyticsService;
import com.store.storeanalyticsapi.transferobjects.AnalyticsTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest(excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, AnalyticsController.class
})
class AnalyticsGetMockMvcTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.analyticsService);
    }

    @Test
    void analyticsGet() throws Exception {

        final List<AnalyticsModel> analyticsModels = PODAM_FACTORY.manufacturePojo(List.class, AnalyticsModel.class);

        when(this.analyticsService.findAll()).thenReturn(analyticsModels);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ANALYTICS))
                .andExpect(status().isOk());

        final List<AnalyticsTO> result = objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, AnalyticsTO.class));

        assertThat(result).isNotEmpty();
        assertThat(result).containsAll(AnalyticsMapper.mapReposToAnalyticsTOs(analyticsModels));

        verify(this.analyticsService).findAll();
    }

    @Test
    void analyticsAnalyticsIdGet() throws Exception {

        final AnalyticsModel analyticsModel = PODAM_FACTORY.manufacturePojo(AnalyticsModel.class);
        final String id = analyticsModel.getId();

        when(this.analyticsService.findById(id)).thenReturn(analyticsModel);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ANALYTICS_WITH_ID, id))
                .andExpect(status().isOk());

        final AnalyticsTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AnalyticsTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(AnalyticsMapper.mapRepoToAnalyticsTO(analyticsModel));

        verify(this.analyticsService).findById(id);
    }

}
