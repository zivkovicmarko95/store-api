package com.store.storeproductapi.controllers.internals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.CategoryControllerHelper;
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
    GlobalExceptionHandler.class, InternalCategoryController.class
} )
class CategoryInternalDeleteByIdMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryControllerHelper categoryControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(categoryControllerHelper);
    }

    @Test
    void internalCategoriesCategoryIdDelete() throws Exception {

        final DeleteResultTO deleteResult = PODAM_FACTORY.manufacturePojo(DeleteResultTO.class);
        final String categoryId = deleteResult.getResourceId();

        when(categoryControllerHelper.internalCategoriesCategoryIdDelete(categoryId))
                .thenReturn(deleteResult);

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_CATEGORIES_WITH_ID, categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("resourceId").value(deleteResult.getResourceId()))
                .andExpect(jsonPath("message").value(deleteResult.getMessage()));

        verify(categoryControllerHelper).internalCategoriesCategoryIdDelete(categoryId);
    }
    
}
