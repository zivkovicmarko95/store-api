package com.store.storeproductapi.controllers.internals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.ProductControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

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
    GlobalExceptionHandler.class, InternalProductController.class
} )
class ProductInternalDeleteByIdMockMvcTest {
    
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
    void internalProductsProductIdDelete() throws Exception {

        final DeleteResultTO deleteResultTO = PODAM_FACTORY.manufacturePojo(DeleteResultTO.class);
        final String productId = deleteResultTO.getResourceId();

        when(this.productControllerHelper.internalProductsProductIdDelete(productId))
                .thenReturn(deleteResultTO);

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_PRODUCTS_WITH_ID, productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(deleteResultTO.getMessage()))
                .andExpect(jsonPath("resourceId").value(deleteResultTO.getResourceId()));

        verify(this.productControllerHelper).internalProductsProductIdDelete(productId);
    }

}
