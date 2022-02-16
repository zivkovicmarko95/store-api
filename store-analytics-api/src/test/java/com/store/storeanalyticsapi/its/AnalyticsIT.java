package com.store.storeanalyticsapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storeanalyticsapi.StoreAnalyticsApiApplication;
import com.store.storeanalyticsapi.constants.ApiTestConstants;
import com.store.storeanalyticsapi.mappers.AnalyticsMapper;
import com.store.storeanalyticsapi.models.AnalyticsModel;
import com.store.storeanalyticsapi.repositories.AnalyticsRepository;
import com.store.storeanalyticsapi.transferobjects.AnalyticsTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = StoreAnalyticsApiApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc( addFilters = false )
class AnalyticsIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final AnalyticsModel ANALYTICS_MODEL = PODAM_FACTORY.manufacturePojo(AnalyticsModel.class);
    private static final String ANALYTICS_ID = ANALYTICS_MODEL.getId();

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.analyticsRepository.save(ANALYTICS_MODEL);
    }
    
    @AfterEach
    void after() {
        this.analyticsRepository.deleteById(ANALYTICS_ID);
    }

    @Test
    void analyticsGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ANALYTICS))
                .andExpect(status().isOk());

        final List<AnalyticsTO> result = this.objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, AnalyticsTO.class));

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).containsAll(AnalyticsMapper.mapReposToAnalyticsTOs(List.of(ANALYTICS_MODEL)));
    }

    @Test
    void analyticsAnalyticsIdGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ANALYTICS_WITH_ID, ANALYTICS_ID))
                .andExpect(status().isOk());

        final AnalyticsTO result = this.objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AnalyticsTO.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(AnalyticsMapper.mapRepoToAnalyticsTO(ANALYTICS_MODEL));
    }

    @Test
    void analyticsAnalyticsIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.ANALYTICS_WITH_ID, ANALYTICS_ID))
                .andExpect(status().isNoContent());

        final Optional<AnalyticsModel> optionalAnalytics = this.analyticsRepository.findById(ANALYTICS_ID);

        assertThat(optionalAnalytics).isEmpty();
    }

}
