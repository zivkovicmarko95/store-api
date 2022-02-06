package com.store.storeorderapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.transferobjects.OrderTO;
import com.store.storeorderapi.transferobjects.OrderedProductTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class OrderMapperTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToOrderTO() {

        final OrderModel orderModel = PODAM_FACTORY.manufacturePojo(OrderModel.class);

        final OrderTO result = OrderMapper.mapRepoToOrderTO(orderModel);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderModel.getId());
        assertThat(result.getCreatedOrder()).isEqualTo(orderModel.getCreatedOrder());

        verifyOrderedProducts(result.getOrderedProducts(), orderModel.getOrderedProducts());
    }

    @Test
    void mapOrderedProductsToRepoOrderedProducts() {

        final Set<com.store.storeorderapi.models.api.OrderCreate.OrderedProducts> orderedProducts = 
                PODAM_FACTORY.manufacturePojo(Set.class, com.store.storeorderapi.models.api.OrderCreate.OrderedProducts.class);

        final Set<OrderedProducts> result = OrderMapper.mapOrderedProductsToRepoOrderedProducts(orderedProducts);

        result.forEach(resultOrderProducts -> {

            final com.store.storeorderapi.models.api.OrderCreate.OrderedProducts orderedProduct = orderedProducts.stream()
                    .filter(product -> product.getId().equals(resultOrderProducts.getId()))
                    .findAny()
                    .get();

            assertThat(resultOrderProducts.getDiscount()).isEqualTo(orderedProduct.getDiscount());
            assertThat(resultOrderProducts.getId()).isEqualTo(orderedProduct.getId());
            assertThat(resultOrderProducts.getPrice()).isEqualTo(orderedProduct.getPrice());
            assertThat(resultOrderProducts.getQuantity()).isEqualTo(orderedProduct.getQuantity());
        });
    }

    private void verifyOrderedProducts(final Set<OrderedProductTO> result, final Set<OrderedProducts> orderedProducts) {

        result.forEach(resultProduct -> {

            final OrderedProducts orderedProduct = orderedProducts.stream()
                    .filter(product -> product.getId().equals(resultProduct.getId()))
                    .findAny()
                    .get();

            assertThat(resultProduct.getDiscount()).isEqualTo(orderedProduct.getDiscount());
            assertThat(resultProduct.getId()).isEqualTo(orderedProduct.getId());
            assertThat(resultProduct.getPrice()).isEqualTo(orderedProduct.getPrice());
            assertThat(resultProduct.getQuantity()).isEqualTo(orderedProduct.getQuantity());
        });

        assertThat(result).hasSameSizeAs(orderedProducts);
    }

}
