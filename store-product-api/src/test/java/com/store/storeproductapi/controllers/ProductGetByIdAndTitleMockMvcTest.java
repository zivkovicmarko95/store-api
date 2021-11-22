package com.store.storeproductapi.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration( classes = {
    GlobalExceptionHandler.class, ProductController.class
} )
class ProductGetByIdAndTitleMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

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

        final ProductTO productTO = PODAM_FACTORY.manufacturePojo(ProductTO.class);
        final String id = productTO.getId();
        final String title = productTO.getTitle();

        when(productControllerHelper.productsProductIdAndTitleGet(id, title))
                .thenReturn(productTO);

        this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH)
                    .param("id", id)
                    .param("title", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("avgUserRating").value(productTO.getAvgUserRating()))
                .andExpect(jsonPath("description").value(productTO.getDescription()))
                .andExpect(jsonPath("imgUrl").value(productTO.getImgUrl()))
                .andExpect(jsonPath("numberOfVotes").value(productTO.getNumberOfVotes()))
                .andExpect(jsonPath("price").value(productTO.getPrice()))
                .andExpect(jsonPath("quantity").value(productTO.getQuantity()));

        verify(this.productControllerHelper).productsProductIdAndTitleGet(id, title);
    }

    @Test
    void productsProductIdAndTitleGet_with_id_only() throws Exception {

        final ProductTO productTO = PODAM_FACTORY.manufacturePojo(ProductTO.class);
        final String id = productTO.getId();

        when(productControllerHelper.productsProductIdAndTitleGet(id, null))
                .thenReturn(productTO);

        this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH)
                    .param("id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(productTO.getTitle()))
                .andExpect(jsonPath("avgUserRating").value(productTO.getAvgUserRating()))
                .andExpect(jsonPath("description").value(productTO.getDescription()))
                .andExpect(jsonPath("imgUrl").value(productTO.getImgUrl()))
                .andExpect(jsonPath("numberOfVotes").value(productTO.getNumberOfVotes()))
                .andExpect(jsonPath("price").value(productTO.getPrice()))
                .andExpect(jsonPath("quantity").value(productTO.getQuantity()));

        verify(this.productControllerHelper).productsProductIdAndTitleGet(id, null);
    }

    @Test
    void productsProductIdAndTitleGet_with_title_only() throws Exception {

        final ProductTO productTO = PODAM_FACTORY.manufacturePojo(ProductTO.class);
        final String title = productTO.getTitle();

        when(productControllerHelper.productsProductIdAndTitleGet(null, title))
                .thenReturn(productTO);

        this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH)
                    .param("title", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(productTO.getId()))
                .andExpect(jsonPath("title").value(productTO.getTitle()))
                .andExpect(jsonPath("avgUserRating").value(productTO.getAvgUserRating()))
                .andExpect(jsonPath("description").value(productTO.getDescription()))
                .andExpect(jsonPath("imgUrl").value(productTO.getImgUrl()))
                .andExpect(jsonPath("numberOfVotes").value(productTO.getNumberOfVotes()))
                .andExpect(jsonPath("price").value(productTO.getPrice()))
                .andExpect(jsonPath("quantity").value(productTO.getQuantity()));

        verify(this.productControllerHelper).productsProductIdAndTitleGet(null, title);
    }

    @Test
    void productsProductIdAndTitleGet_no_params() throws Exception {

        this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH))
                .andExpect(status().isBadRequest());
    }

}
