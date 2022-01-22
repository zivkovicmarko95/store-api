package com.store.storeproductapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.WishlistControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.transferobjects.WishlistTO;

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
    GlobalExceptionHandler.class, WishlistController.class
} )
class WishlistRemoveProductByIdMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistControllerHelper wishlistControllerHelper;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.wishlistControllerHelper);
    }

    @Test
    void wishlistsWishlistIdRemoveProductIdDelete() throws Exception {

        final WishlistTO wishlist = PODAM_FACTORY.manufacturePojo(WishlistTO.class);
        final String wishlistId = wishlist.getId();
        final String productId = wishlist.getProducts().stream().findAny()
                .get()
                .getId();

        when(this.wishlistControllerHelper.wishlistsWishlistIdRemoveProductIdDelete(wishlistId, productId))
                .thenReturn(wishlist);

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.WISHLISTS_WITH_ID_REMOVE_PRODUCT_ID, wishlistId, productId))
                .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(wishlist);

        verify(this.wishlistControllerHelper).wishlistsWishlistIdRemoveProductIdDelete(wishlistId, productId);
    }

}
