package com.store.storeorderapi.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.storeorderapi.constants.ApiTestConstants;
import com.store.storeorderapi.exceptions.handlers.GlobalExceptionHandler;
import com.store.storeorderapi.services.OrderService;

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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest(excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
    GlobalExceptionHandler.class, OrderController.class
})
class OrderDeleteMockMvcTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.orderService);
    }

    @Test
    void ordersOrderIdDelete() throws Exception {

        final String orderId = PODAM_FACTORY.manufacturePojo(String.class);

        this.mockMvc.perform(delete(ApiTestConstants.ORDERS_WITH_ID, orderId))
                .andExpect(jsonPath("$").doesNotHaveJsonPath())
                .andExpect(status().isNoContent());

        verify(this.orderService).removeOrderById(orderId);
    }

}
