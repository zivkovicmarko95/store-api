package com.store.storeorderapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.storeorderapi.constants.ApiTestConstants;
import com.store.storeorderapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
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
class OrderGetMockMvcTest {
 
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
    void ordersOrderIdGet() throws Exception {

        final OrderModel orderModel = PODAM_FACTORY.manufacturePojo(OrderModel.class);
        final String id = orderModel.getId();

        when(this.orderService.getOrderById(id)).thenReturn(orderModel);

        final ResultActions resultActions = this.mockMvc.perform(get(ApiTestConstants.ORDERS_WITH_ID, id))
                .andExpect(status().isOk());

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

        verify(this.orderService).getOrderById(id);
    }

}
