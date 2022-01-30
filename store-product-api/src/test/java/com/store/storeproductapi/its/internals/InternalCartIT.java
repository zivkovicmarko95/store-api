package com.store.storeproductapi.its.internals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.Set;

import com.store.storeproductapi.StoreProductApiApplication;
import com.store.storeproductapi.constants.ApiTestConstants;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.repositories.CartRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = StoreProductApiApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc( addFilters = false )
class InternalCartIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final CartModel CART_MODEL = PODAM_FACTORY.manufacturePojo(CartModel.class)
            .cartProducts(Set.of());
    private static final String CART_ID = CART_MODEL.getId();

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.cartRepository.save(CART_MODEL);
    }

    @AfterEach
    void after() {
        this.cartRepository.deleteById(CART_ID);
    }

    @Test
    void internalCartsCartIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.INTERNAL_CART_WITH_ID, CART_ID))
                .andExpect(status().isNoContent());

        final Optional<CartModel> optionalCart = this.cartRepository.findById(CART_ID);

        assertThat(optionalCart).isEmpty();
    }

}
