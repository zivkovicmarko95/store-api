package com.store.storeanalyticsapi.transferobjects;

import java.util.Objects;

public class AnalyticsProductTO {
    
    private String id;
    private int views;
    private boolean isBought;

    public AnalyticsProductTO() {
    }

    public AnalyticsProductTO(String id, int views, boolean isBought) {
        this.id = id;
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

    public AnalyticsProductTO id(String id) {
        this.id = id;
        return this;
    }

    public AnalyticsProductTO views(int views) {
        this.views = views;
        return this;
    }

    public AnalyticsProductTO isBought(boolean isBought) {
        this.isBought = isBought;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AnalyticsProductTO)) {
            return false;
        }
        AnalyticsProductTO analyticsProductTO = (AnalyticsProductTO) o;
        return Objects.equals(id, analyticsProductTO.id) && 
                views == analyticsProductTO.views && 
                isBought == analyticsProductTO.isBought;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, views, isBought);
    }

    @Override
    public String toString() {
        return "AnalyticsProductTO {" +
            " id='" + getId() + "'" +
            ", views='" + getViews() + "'" +
            ", isBought='" + isIsBought() + "'" +
            "}";
    }

}
