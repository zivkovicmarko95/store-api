package com.store.storeorderapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeorderapi.constants.ApiTestConstants;
import com.store.storeorderapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeorderapi.mappers.OrderMapper;
import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.models.api.OrderCreate;
import com.store.storeorderapi.services.OrderService;
import com.store.storeorderapi.transferobjects.OrderTO;

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

@WebMvcTest(excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, OrderController.class
})
class OrderCreateMockMvcTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.orderService);
    }

    @Test
    void ordersPost() throws Exception {

        final OrderCreate orderCreate = PODAM_FACTORY.manufacturePojoWithFullData(OrderCreate.class);
        final Set<OrderedProducts> orderedProducts = OrderMapper.mapOrderedProductsToRepoOrderedProducts(orderCreate.getOrderedProducts());
        final OrderModel orderModel = PODAM_FACTORY.manufacturePojo(OrderModel.class);
        
        when(this.orderService.saveOrder(orderedProducts)).thenReturn(orderModel);

        final ResultActions resultActions = this.mockMvc.perform(post(ApiTestConstants.ORDERS)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(orderCreate)))
                .andExpect(status().isCreated());

        final OrderTO result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), OrderTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderModel.getId());
        assertThat(result.getCreatedOrder()).isEqualTo(orderModel.getCreatedOrder());

        result.getOrderedProducts().forEach(resultOrderedProduct -> {

            final OrderedProducts orderedProduct = orderModel.getOrderedProducts().stream()
                    .filter(product -> product.getId().equals(resultOrderedProduct.getId()))
                    .findAny()
                    .get();

            assertThat(resultOrderedProduct.getId()).isEqualTo(orderedProduct.getId());
            assertThat(resultOrderedProduct.getDiscount()).isEqualTo(orderedProduct.getDiscount());
            assertThat(resultOrderedProduct.getPrice()).isEqualTo(orderedProduct.getPrice());
            assertThat(resultOrderedProduct.getQuantity()).isEqualTo(orderedProduct.getQuantity());
        });

        verify(this.orderService).saveOrder(orderedProducts);
    }

    @Test
    void ordersPost_noRequestBody() throws Exception {

        this.mockMvc.perform(post(ApiTestConstants.ORDERS))
                .andExpect(status().isBadRequest());
    }

}
