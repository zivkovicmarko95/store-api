package com.store.storeproductapi.controllers.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.controllers.helpers.CategoryControllerHelper;
import com.store.storeproductapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeproductapi.models.api.CategoryCreate;
import com.store.storeproductapi.transferobjects.CategoryTO;

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
    GlobalExceptionHandler.class, InternalCategoryController.class
} )
class CategoryInternalCreateMockMvcTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryControllerHelper categoryControllerHelper;

    @AfterEach
    void setup() {
        verifyNoMoreInteractions(categoryControllerHelper);
    }

    @Test
    void internalCategoriesPost() throws Exception {

        final CategoryCreate categoryCreate = PODAM_FACTORY.manufacturePojo(CategoryCreate.class);
        final CategoryTO categoryTO = PODAM_FACTORY.manufacturePojo(CategoryTO.class)
                .products(Set.of());

        when(this.categoryControllerHelper.internalCategoriesPost(categoryCreate))
                .thenReturn(categoryTO);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(categoryTO.getId()))
                .andExpect(jsonPath("description").value(categoryTO.getDescription()))
                .andExpect(jsonPath("title").value(categoryTO.getTitle()));

        final CategoryTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CategoryTO.class);

        assertThat(result.getProducts()).isEqualTo(categoryTO.getProducts());

        verify(this.categoryControllerHelper).internalCategoriesPost(categoryCreate);
    }

    @Test
    void internalAccountsPost_missingRequestBody() throws Exception {

        this.mockMvc.perform(post(ApiTestConstants.INTERNAL_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
