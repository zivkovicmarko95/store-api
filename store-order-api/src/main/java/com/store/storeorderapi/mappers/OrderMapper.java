package com.store.storeorderapi.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.transferobjects.OrderTO;
import com.store.storeorderapi.transferobjects.OrderedProductTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

public class OrderMapper {
    
    private OrderMapper() {

    }

    public static OrderTO mapRepoToOrderTO(final OrderModel orderModel) {
        ArgumentVerifier.verifyNotNull(orderModel);

        return new OrderTO()
                .id(orderModel.getId())
                .orderedProducts(mapRepoToOrderedProductsTO(orderModel.getOrderedProducts()))
                .createdOrder(orderModel.getCreatedOrder());
    }

    public static Set<OrderedProducts> mapOrderedProductsToRepoOrderedProducts(final Set<com.store.storeorderapi.models.api.OrderCreate.OrderedProducts> orderedProducts) {
        ArgumentVerifier.verifyNotEmpty(orderedProducts);

        return orderedProducts.stream()
                .map(OrderMapper::mapOrderedProductToRepoOrderProduct)
                .collect(Collectors.toSet());
    }

    private static OrderedProducts mapOrderedProductToRepoOrderProduct(final com.store.storeorderapi.models.api.OrderCreate.OrderedProducts orderedProduct) {
        return new OrderedProducts(orderedProduct.getId(), orderedProduct.getQuantity(), orderedProduct.getPrice(), orderedProduct.getDiscount());
    }

    private static Set<OrderedProductTO> mapRepoToOrderedProductsTO(final Set<OrderedProducts> orderedProducts) {

        return orderedProducts.stream()
                .map(OrderMapper::mapRepoToOrderedProductTO)
                .collect(Collectors.toSet());
    }

    private static OrderedProductTO mapRepoToOrderedProductTO(final OrderedProducts orderedProduct) {
        
        return new OrderedProductTO()
                .id(orderedProduct.getId())
                .discount(orderedProduct.getDiscount())
                .price(orderedProduct.getPrice())
                .quantity(orderedProduct.getQuantity());
    }

}
