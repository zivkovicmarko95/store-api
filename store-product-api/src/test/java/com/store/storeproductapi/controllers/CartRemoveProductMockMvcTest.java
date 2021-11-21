package com.store.storeproductapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.CartControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.models.api.Cart;
import com.store.storeproductapi.transferobjects.CartTO;

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
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest( excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class } )
@AutoConfigureMockMvc( addFilters = false )
@ContextConfiguration( classes = {
    GlobalExceptionHandler.class, CartController.class
} )
class CartRemoveProductMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartControllerHelper cartControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(cartControllerHelper);
    }

    @Test
    void cartsRemoveDelete() throws Exception {

        final Cart cart = PODAM_FACTORY.manufacturePojo(Cart.class);
        final CartTO cartTO = PODAM_FACTORY.manufacturePojo(CartTO.class);

        when(cartControllerHelper.cartsRemoveDelete(cart))
                .thenReturn(cartTO);

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.CARTS_REMOVE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("id").value(cartTO.getId()));

        final CartTO resultCart = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CartTO.class);

        assertThat(resultCart.getCartProducts()).isNotNull();
        assertThat(resultCart.getCartProducts()).isNotEmpty();
        assertThat(resultCart.getCartProducts()).containsAll(cartTO.getCartProducts());

        verify(this.cartControllerHelper).cartsRemoveDelete(cart);
    }

    @Test
    void cartsAddPost_missingRequestBody() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.CARTS_REMOVE))
                .andExpect(status().isBadRequest());
                
    }

}
