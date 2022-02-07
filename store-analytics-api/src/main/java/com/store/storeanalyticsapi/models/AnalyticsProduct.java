package com.store.storeanalyticsapi.models;

import java.util.Objects;

public class AnalyticsProduct {
    
    private String id;
    private int views;
    private boolean isBought;

    public AnalyticsProduct() {
    }

    public AnalyticsProduct(int views, boolean isBought) {
        this.views = views;
        this.isBought = isBought;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getViews() {
        return this.views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isIsBought() {
        return this.isBought;
    }

    public boolean getIsBought() {
        return this.isBought;
    }

    public void setIsBought(boolean isBought) {
        this.isBought = isBought;
    }

    public AnalyticsProduct id(String id) {
        this.id = id;
        return this;
    }

    public AnalyticsProduct views(int views) {
        this.views = views;
        return this;
    }

    public AnalyticsProduct isBought(boolean isBought) {
        this.isBought = isBought;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AnalyticsProduct)) {
            return false;
        }
        AnalyticsProduct analyticsProduct = (AnalyticsProduct) o;
        return Objects.equals(id, analyticsProduct.id) && 
                views == analyticsProduct.views && 
                isBought == analyticsProduct.isBought;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, views, isBought);
    }

    @Override
    public String toString() {
        return "AnalyticsProduct {" +
            " id='" + getId() + "'" +
            ", views='" + getViews() + "'" +
            ", isBought='" + isIsBought() + "'" +
            "}";
    }


}
