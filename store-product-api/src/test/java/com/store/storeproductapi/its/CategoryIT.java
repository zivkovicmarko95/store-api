package com.store.storeproductapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.repositories.CategoryRepository;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.transferobjects.CategoryTO;
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
class CategoryIT {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final CategoryModel CATEGORY_MODEL = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
    private static final String CATEGORY_ID = CATEGORY_MODEL.getId();

    private static Set<ProductModel> PRODUCT_MODELS = createProducts();

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        this.categoryRepository.save(CATEGORY_MODEL);
        this.productRepository.saveAll(PRODUCT_MODELS);
    }

    @AfterEach
    void after() {
        this.categoryRepository.delete(CATEGORY_MODEL);
        this.productRepository.deleteAll(PRODUCT_MODELS);
    }

    @Test
    void categoriesGet() throws Exception {

        final ResultActions resultActions = mockMvc.perform(get(ApiTestConstants.CATEGORIES))
                .andExpect(status().isOk());

        final CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(Set.class, CategoryTO.class);
        final Set<CategoryTO> resultCategories = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);
        final CategoryTO resultCategory = resultCategories.stream().filter(category -> category.getId().equals(CATEGORY_ID))
                .findAny().get();

        assertThat(resultCategories).isNotEmpty();
        assertCategory(resultCategory);
    }

    @Test
    void categoriesCategoryIdGet() throws Exception {

        final ResultActions resultActions = mockMvc.perform(get(ApiTestConstants.CATEGORIES_WITH_ID, CATEGORY_ID))
                .andExpect(status().isOk());

        final CategoryTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), CategoryTO.class);

        assertCategory(result);
    }

    @Test
    void categoriesCategoryIdGet_categoryNotFound() throws Exception {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        final ResultActions resultActions = mockMvc.perform(get(ApiTestConstants.CATEGORIES_WITH_ID, id))
                .andExpect(status().isNotFound());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage())
            .isEqualTo(String.format("Resource is not found.Category with id %s is not found", id));
    }

    private void assertCategory(final CategoryTO result) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(CATEGORY_MODEL.getId());
        assertThat(result.getDescription()).isEqualTo(CATEGORY_MODEL.getDescription());
        assertThat(result.getTitle()).isEqualTo(CATEGORY_MODEL.getTitle());

        result.getProducts().forEach(product -> {
            
            final String id = CATEGORY_MODEL.getProductIds().stream()
                    .filter(productId -> productId.equals(product.getId()))
                    .findFirst().get();

            assertThat(id).isNotNull();
            assertThat(id).isEqualTo(product.getId());
        });
    }

    private static Set<ProductModel> createProducts() {
        
        final Set<ProductModel> products = new HashSet<>();

        CATEGORY_MODEL.getProductIds().forEach(productId -> {
            final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class)
                    .id(productId);

            products.add(productModel);
        });

        return products;
    }

}
