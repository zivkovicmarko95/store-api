package com.store.storeproductapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.WishlistControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.transferobjects.WishlistTO;
import com.store.storesharedmodule.models.HttpResponse;

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
class WishlistGetByIdMockMvcTest {
    
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
    void wishlistsWishlistIdAndAccountIdGet() throws Exception {

        final String wishlistId = PODAM_FACTORY.manufacturePojo(String.class);
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final WishlistTO wishlistTO = PODAM_FACTORY.manufacturePojo(WishlistTO.class);

        when(wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(wishlistId, accountId))
                .thenReturn(wishlistTO);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS)
                .param("wishlistId", wishlistId)
                .param("accountId", accountId))
            .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(wishlistTO);

        verify(this.wishlistControllerHelper).wishlistsWishlistIdAndAccountIdGet(wishlistId, accountId);
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_onlyWishlistId() throws Exception {

        final String wishlistId = PODAM_FACTORY.manufacturePojo(String.class);
        final WishlistTO wishlistTO = PODAM_FACTORY.manufacturePojo(WishlistTO.class);

        when(wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(wishlistId, null))
                .thenReturn(wishlistTO);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS)
                .param("wishlistId", wishlistId))
            .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(wishlistTO);

        verify(this.wishlistControllerHelper).wishlistsWishlistIdAndAccountIdGet(wishlistId, null);
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_onlyAccountId() throws Exception {

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final WishlistTO wishlistTO = PODAM_FACTORY.manufacturePojo(WishlistTO.class);

        when(wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(null, accountId))
                .thenReturn(wishlistTO);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS)
                .param("accountId", accountId))
            .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(wishlistTO);

        verify(this.wishlistControllerHelper).wishlistsWishlistIdAndAccountIdGet(null, accountId);
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_noAccountIdNoWishlistId() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS))
            .andExpect(status().isBadRequest());

        final HttpResponse httpResponse = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(httpResponse.getMessage()).isEqualTo("Invalid parameters. Please provide wishlist ID or account ID, or both parameters.");
    }

}
