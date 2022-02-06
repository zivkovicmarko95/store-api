package com.store.storeproductapi.its.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.Product;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.transferobjects.ProductTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@AutoConfigureMockMvc( addFilters = false )
class InternalProductIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final ProductModel PRODUCT_MODEL = PODAM_FACTORY.manufacturePojo(ProductModel.class);
    private static final String PRODUCT_ID = PRODUCT_MODEL.getId();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        this.productRepository.save(PRODUCT_MODEL);
    }

    @AfterEach
    void after() {
        this.productRepository.deleteById(PRODUCT_ID);
    }

    @Test
    void internalProductsPost() throws Exception {

        final Product product = PODAM_FACTORY.manufacturePojo(Product.class);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_PRODUCTS)
                    .content(objectMapper.writeValueAsString(product))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        final ProductTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ProductTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getTitle()).isEqualTo(product.getTitle());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());

        this.productRepository.deleteById(result.getId());
    }

    @Test
    void internalProductsProductIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_PRODUCTS_WITH_ID, PRODUCT_ID))
                .andExpect(status().isOk());

        final ProductModel product = this.productRepository.findById(PRODUCT_ID).get();

        assertThat(product).isNotNull();
        assertThat(product.getVisible()).isFalse();
    }

    @Test
    void internalProductsProductIdRemoveDelete() throws Exception {

        this.productRepository.save(PRODUCT_MODEL.visible(false));

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_PRODUCTS_WITH_ID_REMOVE, PRODUCT_ID))
                .andExpect(status().isNoContent());

        final Optional<ProductModel> optionalProduct = this.productRepository.findById(PRODUCT_ID);

        assertThat(optionalProduct).isEmpty();
    }

    @Test
    void internalProductsProductIdRemoveDelete_productVisible() throws Exception {

        this.productRepository.save(PRODUCT_MODEL.visible(true));

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_PRODUCTS_WITH_ID_REMOVE, PRODUCT_ID))
                .andExpect(status().isBadRequest());
    }

}
