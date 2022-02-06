package com.store.storeorderapi.services;

import java.util.Set;

import com.store.storeorderapi.exceptions.OrderNotFoundException;
import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.repositories.OrderRepository;
import com.store.storesharedmodule.annotations.StoreMongoTransaction;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderModel getOrderById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return this.orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                    String.format("Order with id %s is not found.", id)
                ));
    }

    @StoreMongoTransaction
    public OrderModel saveOrder(final Set<OrderedProducts> orderedProducts) {
        ArgumentVerifier.verifyNotEmpty(orderedProducts);

        return this.orderRepository.save(
            new OrderModel(orderedProducts)
        );
    }

    @StoreMongoTransaction
    public void removeOrderById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        LOGGER.info("Removing Order with id {}.", id);

        this.orderRepository.deleteById(id);
    }

}
