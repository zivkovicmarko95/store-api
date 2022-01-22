package com.store.storeproductapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.mappers.ProductMapper;
import com.store.storeproductapi.mappers.WishlistMapper;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.models.api.WishlistCreate;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.repositories.WishlistRepository;
import com.store.storeproductapi.transferobjects.WishlistTO;

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
@AutoConfigureMockMvc(addFilters = false)
class WishlistIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ProductModel PRODUCT_MODEL = PODAM_FACTORY.manufacturePojo(ProductModel.class);
    private final String PRODUCT_ID = PRODUCT_MODEL.getId();

    private final WishlistModel WISHLIST_MODEL = PODAM_FACTORY.manufacturePojo(WishlistModel.class)
            .productIds(Set.of(PRODUCT_ID));
    private final String WISHLIST_ID = WISHLIST_MODEL.getId();
    private final String ACCOUNT_ID = WISHLIST_MODEL.getAccountId();

    private final AccountModel ACCOUNT_MODEL = PODAM_FACTORY.manufacturePojo(AccountModel.class)
            .id(ACCOUNT_ID);

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.productRepository.save(PRODUCT_MODEL);
        this.wishlistRepository.save(WISHLIST_MODEL);
        this.accountRepository.save(ACCOUNT_MODEL);
    }

    @AfterEach
    void after() {
        this.productRepository.delete(PRODUCT_MODEL);
        this.wishlistRepository.delete(WISHLIST_MODEL);
        this.accountRepository.delete(ACCOUNT_MODEL);
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_with_id_and_accountId() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS)
                    .param("wishlistId", WISHLIST_ID)
                    .param("accountId", ACCOUNT_ID))
                .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(WishlistMapper.mapRepoToWishlistTO(WISHLIST_MODEL, Set.of(PRODUCT_MODEL)));
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_with_id() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS)
                    .param("wishlistId", WISHLIST_ID))
                .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(WishlistMapper.mapRepoToWishlistTO(WISHLIST_MODEL, Set.of(PRODUCT_MODEL)));
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_with_accountId() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.WISHLISTS)
                    .param("accountId", ACCOUNT_ID))
                .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isEqualTo(WishlistMapper.mapRepoToWishlistTO(WISHLIST_MODEL, Set.of(PRODUCT_MODEL)));
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_no_accountId_no_wishlistId() throws Exception {

        this.mockMvc.perform(get(ApiTestConstants.WISHLISTS))
                .andExpect(status().isBadRequest());
    }

    @Test
    void wishlistsPost() throws Exception {

        final WishlistCreate wishlistCreate = new WishlistCreate()
                .accountId(ACCOUNT_ID)
                .productId(PRODUCT_ID);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.WISHLISTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(wishlistCreate)))
                .andExpect(status().isCreated());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getProducts()).containsOnly(ProductMapper.mapRepoToProductTO(PRODUCT_MODEL));
        
        this.wishlistRepository.deleteById(result.getId());
    }

    @Test
    void wishlistsPost_noRequestBody() throws Exception{

        this.mockMvc.perform(post(ApiTestConstants.WISHLISTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void wishlistsWishlistIdAddProductIdPost() throws Exception {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);

        this.productRepository.save(productModel);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.WISHLISTS_WITH_ID_ADD_PRODUCT_ID, WISHLIST_ID, productModel.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isNotNull();

        result.getProducts().forEach(productTO -> {
            
            final ProductModel product = this.productRepository.findById(productTO.getId()).get();

            assertThat(productTO).isEqualTo(ProductMapper.mapRepoToProductTO(product));
        });

        assertThat(result.getProducts()).hasSize(2);

        this.productRepository.delete(productModel);
    }

    @Test
    void wishlistsWishlistIdAddProductIdPost_productNotFound() throws Exception {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        this.mockMvc.perform(post(ApiTestConstants.WISHLISTS_WITH_ID_ADD_PRODUCT_ID, WISHLIST_ID, productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void wishlistsWishlistIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.WISHLISTS_WITH_ID, WISHLIST_ID, WISHLIST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void wishlistsWishlistIdRemoveProductIdDelete() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.WISHLISTS_WITH_ID_REMOVE_PRODUCT_ID, WISHLIST_ID, PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        final WishlistTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), WishlistTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(WISHLIST_ID);
        assertThat(result.getProducts()).isEmpty();
    }

    @Test
    void wishlistsWishlistIdRemoveProductIdDelete_productNotFound() throws Exception {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        this.mockMvc.perform(delete(ApiTestConstants.WISHLISTS_WITH_ID_REMOVE_PRODUCT_ID, WISHLIST_ID, productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
