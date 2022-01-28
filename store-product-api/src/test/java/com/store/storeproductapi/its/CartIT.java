package com.store.storeproductapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.Cart;
import com.store.storeproductapi.models.api.CartCreate;
import com.store.storeproductapi.repositories.AccountRepository;
import com.store.storeproductapi.repositories.CartRepository;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.transferobjects.CartProductTO;
import com.store.storeproductapi.transferobjects.CartTO;
import com.store.storesharedmodule.models.HttpResponse;

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
class CartIT {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    private static final AccountModel ACCOUNT_MODEL = PODAM_FACTORY.manufacturePojo(AccountModel.class);
    private static final CartModel CART_MODEL = PODAM_FACTORY.manufacturePojo(CartModel.class)
            .id(ACCOUNT_MODEL.getCartId())
            .accountId(ACCOUNT_MODEL.getId());
    private static final String CART_ID = CART_MODEL.getId();

    private static final Set<ProductModel> PRODUCTS = createProducts();

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.cartRepository.save(CART_MODEL);
        this.accountRepository.save(ACCOUNT_MODEL);
        this.productRepository.saveAll(PRODUCTS);
    }

    @AfterEach
    void after() {
        this.cartRepository.delete(CART_MODEL);
        this.accountRepository.delete(ACCOUNT_MODEL);
        this.productRepository.deleteAll(PRODUCTS);
    }
    
    @Test
    void cartsCartIdGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.CARTS_WITH_ID, CART_ID))
                .andExpect(status().isOk());

        final CartTO resultCart = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), CartTO.class);

        assertCart(resultCart);
    }

    @Test
    void cartsCartIdGet_notFound() throws Exception {

        final String categoryId = PODAM_FACTORY.manufacturePojo(String.class);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.CARTS_WITH_ID, categoryId))
                .andExpect(status().is5xxServerError());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Server is unavailbale now. Try again later.");
    }

    @Test
    void cartsPost() throws Exception {

        final String productId = PRODUCTS.stream().findAny()
                .get().getId();
        final CartCreate cartCreate = new CartCreate(productId, ACCOUNT_MODEL.getId(), 1);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.CARTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cartCreate)))
                .andExpect(status().isCreated());

        final CartTO resultCart = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), CartTO.class);

        assertThat(resultCart).isNotNull();
        
        final CartProductTO cartProductTO = resultCart.getCartProducts().stream()
                .filter(cartProduct -> cartProduct.getId().equals(productId))
                .findFirst().get();
        
        assertThat(cartProductTO.getId()).isEqualTo(productId);
        assertThat(cartProductTO.getSelectedQuantity()).isEqualTo(cartCreate.getQuantity());

        this.cartRepository.deleteById(resultCart.getId());
    }

    @Test
    void cartsPost_noRequestBody() throws Exception {
        
        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.CARTS)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

    @Test
    void cartsAddPost() throws Exception {

        final String productId = PRODUCTS.stream().findAny()
                .get()
                .getId();
        final Cart cart = new Cart(CART_ID, productId, ACCOUNT_MODEL.getId(), 1);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.CARTS_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cart)))
                .andExpect(status().isOk());

        final CartTO resultCart = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), CartTO.class);

        assertCart(resultCart);
    }

    @Test
    void cartsAddPost_noRequestBody() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.CARTS_ADD)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

    @Test
    void cartsAddPost_productNotExist() throws Exception {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final Cart cart = new Cart(CART_ID, productId, ACCOUNT_MODEL.getId(), 1);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.CARTS_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cart)))
                .andExpect(status().isNotFound());

        final HttpResponse result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(result.getMessage())
            .isEqualTo(String.format("Resource is not found.Product with provided id %s is not found", productId));
    }

    @Test
    void cartsAddPost_accountNotOwnsCart() throws Exception {

        final String productId = PRODUCTS.stream().findAny()
                .get()
                .getId();
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        
        final Cart cart = new Cart(CART_ID, productId, accountId, 1);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.CARTS_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cart)))
                .andExpect(status().isBadRequest());

        final HttpResponse result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(result.getMessage()).isEqualTo("Resource is in invalid state.");
    }

    @Test
    void cartsRemoveDelete() throws Exception {

        final String productId = PRODUCTS.stream().findAny()
                .get()
                .getId();
        final Cart cart = new Cart(CART_ID, productId, ACCOUNT_MODEL.getId(), 1);

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.CARTS_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cart)))
                .andExpect(status().isAccepted());

        final CartTO result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), CartTO.class);

        assertCart(result);
    }

    @Test
    void cartsRemoveDelete_productNotFound() throws Exception {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final Cart cart = new Cart(CART_ID, productId, ACCOUNT_MODEL.getId(), 1);

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.CARTS_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cart)))
                .andExpect(status().isNotFound());

        final HttpResponse result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(result.getMessage())
            .isEqualTo(String.format("Resource is not found.Product with id %s is not found in the cart with id %s", productId, CART_ID));
    }

    @Test
    void cartsRemoveDelete_accountNotOwnsCart() throws Exception {

        final String productId = PRODUCTS.stream().findAny()
                .get()
                .getId();
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        
        final Cart cart = new Cart(CART_ID, productId, accountId, 1);

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.CARTS_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(cart)))
                .andExpect(status().isBadRequest());

        final HttpResponse result = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(result.getMessage()).isEqualTo("Resource is in invalid state.");
    }

    @Test
    void cartsRemoveDelete_noRequestBody() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(delete(ApiTestConstants.CARTS_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        final HttpResponse response = OBJECT_MAPPER.readValue(resultActions.andReturn().getResponse().getContentAsString(), HttpResponse.class);

        assertThat(response.getMessage()).isEqualTo("Provided HTTP message is not readable.");
    }

    private static Set<ProductModel> createProducts() {
        
        final Set<ProductModel> products = new HashSet<>();

        CART_MODEL.getCartProducts().forEach(cartProduct -> {

            final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class)
                    .id(cartProduct.getProductId());

            products.add(productModel);
        });

        return products;
    }

    private void assertCart(CartTO resultCart) {

        assertThat(resultCart).isNotNull();
        assertThat(resultCart.getId()).isEqualTo(CART_MODEL.getId());

        resultCart.getCartProducts().forEach(cartProduct -> {
            
            final CartProductModel foundCartProductModel = CART_MODEL.getCartProducts().stream()
                    .filter(product -> product.getProductId().equals(cartProduct.getId()))
                    .findFirst().get();

            assertThat(foundCartProductModel.getProductId()).isEqualTo(cartProduct.getId());
            assertThat(foundCartProductModel.getSelectedQuantity()).isEqualTo(cartProduct.getSelectedQuantity());
        });
    }

}
