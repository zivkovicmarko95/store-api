package com.store.storeanalyticsapi.transferobjects;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class AnalyticsTO {
    
    private String id;
    private String accountId;
    private Set<AnalyticsProductTO> products;
    private Date createdDate;

    public AnalyticsTO() {
    }

    public AnalyticsTO(String id, String accountId, Set<AnalyticsProductTO> products, 
            Date createdDate) {
        this.id = id;
        this.accountId = accountId;
        this.products = products;
        this.createdDate = createdDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Set<AnalyticsProductTO> getProducts() {
        return this.products;
    }

    public void setProducts(Set<AnalyticsProductTO> products) {
        this.products = products;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public AnalyticsTO id(String id) {
        this.id = id;
        return this;
    }

    public AnalyticsTO accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public AnalyticsTO products(Set<AnalyticsProductTO> products) {
        this.products = products;
        return this;
    }

    public AnalyticsTO createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AnalyticsTO)) {
            return false;
        }
        AnalyticsTO analyticsTO = (AnalyticsTO) o;
        return Objects.equals(id, analyticsTO.id) && 
                Objects.equals(accountId, analyticsTO.accountId) && 
                Objects.equals(products, analyticsTO.products) && 
                Objects.equals(createdDate, analyticsTO.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, products, createdDate);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", products='" + getProducts() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }

}
