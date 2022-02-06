package com.store.storeorderapi.models;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class OrderModel {
    
    @Id
    private String id;
    private Set<OrderedProducts> orderedProducts;
    private Date createdOrder;

    public OrderModel() {
    }

    public OrderModel(final Set<OrderedProducts> orderedProducts) {
        ArgumentVerifier.verifyNotEmpty(orderedProducts);
        
        this.orderedProducts = orderedProducts;
        this.createdOrder = new Date();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<OrderedProducts> getOrderedProducts() {
        return this.orderedProducts;
    }

    public void setOrderedProducts(Set<OrderedProducts> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public Date getCreatedOrder() {
        return this.createdOrder;
    }

    public void setCreatedOrder(Date createdOrder) {
        this.createdOrder = createdOrder;
    }

    public OrderModel id(String id) {
        this.id = id;
        return this;
    }

    public OrderModel orderedProducts(Set<OrderedProducts> orderedProducts) {
        this.orderedProducts = orderedProducts;
        return this;
    }

    public OrderModel createdOrder(Date createdOrder) {
        this.createdOrder = createdOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderModel)) {
            return false;
        }
        OrderModel orderModel = (OrderModel) o;
        return Objects.equals(id, orderModel.id) && 
                Objects.equals(orderedProducts, orderModel.orderedProducts) && 
                Objects.equals(createdOrder, orderModel.createdOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderedProducts, createdOrder);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", orderedProducts='" + getOrderedProducts() + "'" +
            ", createdOrder='" + getCreatedOrder() + "'" +
            "}";
    }

}
