package com.store.storeproductapi.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.WishlistControllerHelper;
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
    GlobalExceptionHandler.class, WishlistController.class
} )
class WishlistDeleteByIdMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistControllerHelper wishlistControllerHelper;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.wishlistControllerHelper);
    }

    @Test
    void wishlistsWishlistIdDelete() throws Exception {

        final String wishlistId = PODAM_FACTORY.manufacturePojo(String.class);
        final DeleteResultTO deleteResultTO = PODAM_FACTORY.manufacturePojo(DeleteResultTO.class);

        when(this.wishlistControllerHelper.wishlistsWishlistIdDelete(wishlistId))
                .thenReturn(deleteResultTO);

        this.mockMvc.perform(delete(ApiTestConstants.WISHLISTS_WITH_ID, wishlistId))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("message").value(deleteResultTO.getMessage()))
            .andExpect(jsonPath("resourceId").value(deleteResultTO.getResourceId()));

        verify(this.wishlistControllerHelper).wishlistsWishlistIdDelete(wishlistId);
    }

}
