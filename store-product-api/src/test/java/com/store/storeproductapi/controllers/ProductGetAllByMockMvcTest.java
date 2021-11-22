package com.store.storeproductapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.ProductControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.transferobjects.ProductTO;

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

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration( classes = {
    GlobalExceptionHandler.class, ProductController.class
} )
class ProductGetAllByMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductControllerHelper productControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(productControllerHelper);
    }

    @Test
    void productsProductIdAndTitleGet() throws Exception {

        final int page = PODAM_FACTORY.manufacturePojo(Integer.class);
        final Set<ProductTO> productTOs = PODAM_FACTORY.manufacturePojo(Set.class, ProductTO.class);

        when(this.productControllerHelper.productsGet(page))
                .thenReturn(productTOs);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS)
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk());

        final Set<ProductTO> resultProducts = objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), 
                objectMapper.getTypeFactory().constructCollectionLikeType(Set.class, ProductTO.class)        
        );

        assertThat(resultProducts).isNotNull();
        assertThat(resultProducts).isNotEmpty();
        assertThat(resultProducts).containsAll(productTOs);

        verify(this.productControllerHelper).productsGet(page);
    }

}
