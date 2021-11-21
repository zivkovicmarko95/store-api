package com.store.storeproductapi.controllers.internals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.ProductControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.models.api.Product;
import com.store.storeproductapi.transferobjects.ProductTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration( classes = {
    GlobalExceptionHandler.class, InternalProductController.class
} )
class ProductInternalCreateMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductControllerHelper productControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(productControllerHelper);
    }

    @Test
    void internalProductsPost() throws Exception {

        final Product product = PODAM_FACTORY.manufacturePojo(Product.class);
        final ProductTO productTO = PODAM_FACTORY.manufacturePojo(ProductTO.class);

        when(this.productControllerHelper.internalProductsPost(product))
                .thenReturn(productTO);

        this.mockMvc.perform(post(ApiTestConstants.INTERNAL_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(productTO.getId()))
                .andExpect(jsonPath("avgUserRating").value(productTO.getAvgUserRating()))
                .andExpect(jsonPath("description").value(productTO.getDescription()))
                .andExpect(jsonPath("imgUrl").value(productTO.getImgUrl()))
                .andExpect(jsonPath("numberOfVotes").value(productTO.getNumberOfVotes()))
                .andExpect(jsonPath("price").value(productTO.getPrice()))
                .andExpect(jsonPath("quantity").value(productTO.getQuantity()))
                .andExpect(jsonPath("title").value(productTO.getTitle()));

        verify(this.productControllerHelper).internalProductsPost(product);
    }

    @Test
    void internalProductsPost_missingRequestBody() throws Exception {

        this.mockMvc.perform(post(ApiTestConstants.INTERNAL_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
