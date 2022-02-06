package com.store.storeorderapi.its;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeorderapi.StoreOrderApiApplication;
import com.store.storeorderapi.constants.ApiTestConstants;
import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.models.api.OrderCreate;
import com.store.storeorderapi.repositories.OrderRepository;
import com.store.storeorderapi.transferobjects.OrderTO;

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
    classes = StoreOrderApiApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc( addFilters = false )
class OrderIT {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private static final OrderModel ORDER_MODEL = PODAM_FACTORY.manufacturePojo(OrderModel.class);
    private static final String ORDER_ID = ORDER_MODEL.getId();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.orderRepository.save(ORDER_MODEL);
    }

    @AfterEach
    void after() {
        this.orderRepository.deleteById(ORDER_ID);
    }

    @Test
    void ordersOrderIdGet() throws Exception {

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ORDERS_WITH_ID, ORDER_ID))
                .andExpect(status().isOk());

        final OrderTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), OrderTO.class);

        verifyOrder(result, ORDER_MODEL);
    }

    @Test
    void ordersOrderIdGet_orderNotFound() throws Exception {
        
        final String orderId = PODAM_FACTORY.manufacturePojo(String.class);

        this.mockMvc.perform(get(ApiTestConstants.ORDERS_WITH_ID, orderId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ordersOrderIdDelete() throws Exception {

        this.mockMvc.perform(delete(ApiTestConstants.ORDERS_WITH_ID, ORDER_ID))
                .andExpect(status().isNoContent());

        final Optional<OrderModel> optionalOrder = this.orderRepository.findById(ORDER_ID);

        assertThat(optionalOrder).isEmpty();
    }

    @Test
    void ordersPost() throws Exception {

        final OrderCreate orderCreate = PODAM_FACTORY.manufacturePojo(OrderCreate.class);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.ORDERS)
                    .content(objectMapper.writeValueAsString(orderCreate))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        final OrderTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), OrderTO.class);

        assertThat(result).isNotNull();
        
        result.getOrderedProducts().forEach(orderedProductsTO -> {

            final com.store.storeorderapi.models.api.OrderCreate.OrderedProducts orderedProducts = orderCreate.getOrderedProducts().stream()
                    .filter(product -> product.getId().equals(orderedProductsTO.getId()))
                    .findAny()
                    .get();

            assertThat(orderedProductsTO.getId()).isEqualTo(orderedProducts.getId());
            assertThat(orderedProductsTO.getDiscount()).isEqualTo(orderedProducts.getDiscount());
            assertThat(orderedProductsTO.getPrice()).isEqualTo(orderedProducts.getPrice());
            assertThat(orderedProductsTO.getQuantity()).isEqualTo(orderedProducts.getQuantity());
        });

        this.orderRepository.deleteById(result.getId());
    }

    @Test
    void ordersPost_noRequestBody() throws Exception {

        this.mockMvc.perform(post(ApiTestConstants.ORDERS))
                .andExpect(status().isBadRequest());
    }

    private void verifyOrder(OrderTO result, OrderModel orderModel) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderModel.getId());
        assertThat(result.getCreatedOrder()).isEqualTo(orderModel.getCreatedOrder());

        result.getOrderedProducts().forEach(orderedProductsTO -> {

            final OrderedProducts orderedProduct = orderModel.getOrderedProducts().stream()
                    .filter(product -> product.getId().equals(orderedProductsTO.getId()))
                    .findAny()
                    .get();

            assertThat(orderedProductsTO.getId()).isEqualTo(orderedProduct.getId());
            assertThat(orderedProductsTO.getDiscount()).isEqualTo(orderedProduct.getDiscount());
            assertThat(orderedProductsTO.getPrice()).isEqualTo(orderedProduct.getPrice());
            assertThat(orderedProductsTO.getQuantity()).isEqualTo(orderedProduct.getQuantity());
        });
    }

}
