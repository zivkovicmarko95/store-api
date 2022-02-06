package com.store.storeproductapi.its.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.CategoryCreate;
import com.store.storeproductapi.repositories.CategoryRepository;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.transferobjects.CategoryTO;
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
class InternalCategoryIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final ProductModel PRODUCT_MODEL = PODAM_FACTORY.manufacturePojo(ProductModel.class);
    private static final String PRODUCT_ID = PRODUCT_MODEL.getId();

    private static final CategoryModel CATEGORY_MODEL = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
    private static final String CATEGORY_ID = CATEGORY_MODEL.getId();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        final Set<String> productIds = new HashSet<>();
        productIds.add(PRODUCT_ID);

        this.productRepository.save(PRODUCT_MODEL);
        this.categoryRepository.save(CATEGORY_MODEL.productIds(productIds));
    }
    
    @AfterEach
    void after() {
        this.productRepository.deleteById(PRODUCT_ID);
        this.categoryRepository.deleteById(CATEGORY_ID);
    }

    @Test
    void internalCategoriesPost() throws Exception {

        final CategoryCreate categoryCreate = PODAM_FACTORY.manufacturePojo(CategoryCreate.class);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_CATEGORIES)
                    .content(objectMapper.writeValueAsString(categoryCreate))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        final CategoryTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CategoryTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(categoryCreate.getDescription());
        assertThat(result.getTitle()).isEqualTo(categoryCreate.getTitle());

        this.categoryRepository.deleteById(result.getId());
    }

    @Test
    void internalCategoriesCategoryIdProductsAssignPost() throws Exception {
        
        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        this.productRepository.save(productModel);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_CATEGORIES_WITH_ID_ASSIGN, CATEGORY_ID)
                    .content(objectMapper.writeValueAsString(Set.of(productModel.getId())))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        final CategoryTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CategoryTO.class);

        assertThat(result).isNotNull();

        final ProductTO productTO = result.getProducts().stream()
                .filter(product -> product.getId().equals(productModel.getId()))
                .findAny()
                .get();

        assertThat(productTO.getId()).isEqualTo(productModel.getId());
        assertThat(productTO.getDescription()).isEqualTo(productModel.getDescription());
        assertThat(productTO.getImgUrl()).isEqualTo(productModel.getImgUrl());
        assertThat(productTO.getAvgUserRating()).isEqualTo(productModel.getAvgUserRating());
        assertThat(productTO.getNumberOfVotes()).isEqualTo(productModel.getNumberOfVotes());
        assertThat(productTO.getPrice()).isEqualTo(productModel.getPrice());
        assertThat(productTO.getQuantity()).isEqualTo(productModel.getQuantity());
        assertThat(productTO.getTitle()).isEqualTo(productModel.getTitle());

        this.productRepository.deleteById(productModel.getId());
    }

    @Test
    void internalCategoriesCategoryIdProductsProductIdPost() throws Exception {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String productId = productModel.getId();
        this.productRepository.save(productModel);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.INTERNAL_CATEGORIES_WITH_ID_PRODUCTS_WITH_PRODUCT_ID, CATEGORY_ID, productId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        final CategoryTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CategoryTO.class);

        assertThat(result).isNotNull();

        final ProductTO productTO = result.getProducts().stream()
                .filter(product -> product.getId().equals(productModel.getId()))
                .findAny()
                .get();

        assertThat(productTO.getId()).isEqualTo(productModel.getId());
        assertThat(productTO.getDescription()).isEqualTo(productModel.getDescription());
        assertThat(productTO.getImgUrl()).isEqualTo(productModel.getImgUrl());
        assertThat(productTO.getAvgUserRating()).isEqualTo(productModel.getAvgUserRating());
        assertThat(productTO.getNumberOfVotes()).isEqualTo(productModel.getNumberOfVotes());
        assertThat(productTO.getPrice()).isEqualTo(productModel.getPrice());
        assertThat(productTO.getQuantity()).isEqualTo(productModel.getQuantity());
        assertThat(productTO.getTitle()).isEqualTo(productModel.getTitle());

        this.productRepository.deleteById(productModel.getId());
    }

    @Test
    void internalCategoriesCategoryIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_CATEGORIES_WITH_ID, CATEGORY_ID))
                .andExpect(status().isOk());

        final CategoryModel optionalCategory = this.categoryRepository.findById(CATEGORY_ID).get();

        assertThat(optionalCategory.getVisible()).isFalse();
    }
    
}
