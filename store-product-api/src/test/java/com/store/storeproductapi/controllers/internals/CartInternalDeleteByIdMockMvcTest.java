package com.store.storeproductapi.controllers.internals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.CartControllerHelper;
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
    GlobalExceptionHandler.class, InternalCartController.class
} )
class CartInternalDeleteByIdMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartControllerHelper cartControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(cartControllerHelper);
    }

    @Test
    void internalCartsCartIdDelete() throws Exception {

        final DeleteResultTO deleteResult = PODAM_FACTORY.manufacturePojo(DeleteResultTO.class);
        final String cartId = deleteResult.getResourceId();

        when(cartControllerHelper.internalCartsCartIdDelete(cartId))
                .thenReturn(deleteResult);

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_CART_WITH_ID, cartId))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("resourceId").value(cartId))
                .andExpect(jsonPath("message").value(deleteResult.getMessage()));

        verify(this.cartControllerHelper).internalCartsCartIdDelete(cartId);
    }

}
