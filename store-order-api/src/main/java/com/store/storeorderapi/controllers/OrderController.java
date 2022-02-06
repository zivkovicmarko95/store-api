package com.store.storeorderapi.controllers;

import java.util.Set;

import com.store.storeorderapi.mappers.OrderMapper;
import com.store.storeorderapi.models.OrderModel;
import com.store.storeorderapi.models.OrderedProducts;
import com.store.storeorderapi.models.api.OrderCreate;
import com.store.storeorderapi.services.OrderService;
import com.store.storeorderapi.transferobjects.OrderTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderTO> ordersOrderIdGet(@PathVariable final String orderId) {

        return new ResponseEntity<>(
                OrderMapper.mapRepoToOrderTO(this.orderService.getOrderById(orderId)),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<OrderTO> ordersPost(@RequestBody final OrderCreate orderCreate) {

        final Set<OrderedProducts> orderedProducts = OrderMapper.mapOrderedProductsToRepoOrderedProducts(orderCreate.getOrderedProducts());

        final OrderModel createdOrder = this.orderService.saveOrder(orderedProducts);

        return new ResponseEntity<>(
                OrderMapper.mapRepoToOrderTO(createdOrder),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> ordersOrderIdDelete(@PathVariable final String orderId) {

        this.orderService.removeOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
