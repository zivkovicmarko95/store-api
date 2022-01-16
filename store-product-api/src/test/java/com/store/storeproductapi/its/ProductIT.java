package com.store.storeproductapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.transferobjects.ProductTO;
import com.store.storesharedmodule.models.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = StoreProductApiApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
class ProductIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ProductModel PRODUCT_MODEL = PODAM_FACTORY.manufacturePojo(ProductModel.class);
    private final String PRODUCT_ID = PRODUCT_MODEL.getId();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.productRepository.save(PRODUCT_MODEL);
    }

    @AfterEach
    void after() {
        this.productRepository.delete(PRODUCT_MODEL);
    }

    @Test
    void productsProductIdAndTitleGet_with_id_and_title() throws Exception {

        final String title = PRODUCT_MODEL.getTitle();

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH)
                    .param("id", PRODUCT_ID)
                    .param("title", title))
                .andExpect(status().isOk());

        final ProductTO resultProduct = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), ProductTO.class);

        assertProduct(resultProduct);
    }

    @Test
    void productsProductIdAndTitleGet_with_id() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH)
                    .param("id", PRODUCT_ID))
                .andExpect(status().isOk());

        final ProductTO resultProduct = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), ProductTO.class);

        assertProduct(resultProduct);
    }

    @Test
    void productsProductIdAndTitleGet_with_title() throws Exception {

        final String title = PRODUCT_MODEL.getTitle();

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH)
                    .param("title", title))
                .andExpect(status().isOk());

        final ProductTO resultProduct = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), ProductTO.class);

        assertProduct(resultProduct);
    }

    @Test
    void productsProductIdAndTitleGet_no_id_no_title() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS_SEARCH))
                .andExpect(status().isBadRequest());

        final HttpResponse result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Invalid parameters. Please provide products ID or products title, or both product parameters.");
    }

    @Test
    void productsGet() throws Exception {

        final String page = "0";

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS)
                    .param("page", page))
                .andExpect(status().isOk());

        final CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(Set.class, ProductTO.class);
        final Set<ProductTO> resultProducts = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);
        final ProductTO resultProduct = resultProducts.stream().filter(product -> product.getId().equals(PRODUCT_ID))
                .findAny().get();

        assertThat(resultProducts).isNotEmpty();
        assertProduct(resultProduct);
    }

    @Test
    void productsGet_invalidNumberFormat() throws Exception {

        final String page = PODAM_FACTORY.manufacturePojo(String.class);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.PRODUCTS)
                    .param("page", page))
                .andExpect(status().isInternalServerError());

        final HttpResponse result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Server is unavailbale now. Try again later.");
    }

    private void assertProduct(final ProductTO result) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(PRODUCT_ID);
        assertThat(result.getTitle()).isEqualTo(PRODUCT_MODEL.getTitle());
        assertThat(result.getAvgUserRating()).isEqualTo(PRODUCT_MODEL.getAvgUserRating());
        assertThat(result.getDescription()).isEqualTo(PRODUCT_MODEL.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(PRODUCT_MODEL.getImgUrl());
        assertThat(result.getNumberOfVotes()).isEqualTo(PRODUCT_MODEL.getNumberOfVotes());
        assertThat(result.getPrice()).isEqualTo(PRODUCT_MODEL.getPrice());
        assertThat(result.getQuantity()).isEqualTo(PRODUCT_MODEL.getQuantity());

    }

}
