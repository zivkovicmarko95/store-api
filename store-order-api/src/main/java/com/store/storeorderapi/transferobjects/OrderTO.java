package com.store.storeorderapi.transferobjects;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class OrderTO {
    
    private String id;
    private Set<OrderedProductTO> orderedProducts;
    private Date createdOrder;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<OrderedProductTO> getOrderedProducts() {
        return this.orderedProducts;
    }

    public void setOrderedProducts(Set<OrderedProductTO> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public Date getCreatedOrder() {
        return this.createdOrder;
    }

    public void setCreatedOrder(Date createdOrder) {
        this.createdOrder = createdOrder;
    }

    public OrderTO id(String id) {
        this.id = id;
        return this;
    }

    public OrderTO orderedProducts(Set<OrderedProductTO> orderedProducts) {
        this.orderedProducts = orderedProducts;
        return this;
    }

    public OrderTO createdOrder(Date createdOrder) {
        this.createdOrder = createdOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTO)) {
            return false;
        }
        OrderTO orderTO = (OrderTO) o;
        return Objects.equals(id, orderTO.id) &&
                Objects.equals(orderedProducts, orderTO.orderedProducts) && 
                Objects.equals(createdOrder, orderTO.createdOrder);
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
