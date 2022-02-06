package com.store.storeorderapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import com.store.storeorderapi.exceptions.OrderNotFoundException;
import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.repositories.OrderRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({  SpringExtension.class, MockitoExtension.class })
class OrderServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderService = new OrderService(this.orderRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.orderRepository);
    }

    @Test
    void getOrderById() {

        final OrderModel orderModel = PODAM_FACTORY.manufacturePojo(OrderModel.class);
        final String id = orderModel.getId();

        when(this.orderRepository.findById(id)).thenReturn(Optional.of(orderModel));

        final OrderModel result = this.orderService.getOrderById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getCreatedOrder()).isInSameDayAs(orderModel.getCreatedOrder());
        assertThat(result.getOrderedProducts()).containsAll(orderModel.getOrderedProducts());

        verify(this.orderRepository).findById(id);
    }

    @Test
    void getOrderById_orderNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.getOrderById(id))
                .isExactlyInstanceOf(OrderNotFoundException.class)
                .hasNoCause();

        verify(this.orderRepository).findById(id);
    }

    @Test
    void saveOrder() {

        final OrderModel orderModel = PODAM_FACTORY.manufacturePojo(OrderModel.class);
        final Set<OrderedProducts> orderedProducts = orderModel.getOrderedProducts();
        
        when(this.orderRepository.save(any())).thenReturn(orderModel);

        final OrderModel result = this.orderService.saveOrder(orderedProducts);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderModel.getId());
        assertThat(result.getCreatedOrder()).isEqualTo(orderModel.getCreatedOrder());
        assertThat(result.getOrderedProducts()).containsAll(orderModel.getOrderedProducts());

        verify(this.orderRepository).save(any());
    }

    @Test
    void removeOrderById() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        this.orderService.removeOrderById(id);

        verify(this.orderRepository).deleteById(id);
    }

}
