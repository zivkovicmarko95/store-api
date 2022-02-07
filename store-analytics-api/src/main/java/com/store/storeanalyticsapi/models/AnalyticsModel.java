package com.store.storeanalyticsapi.models;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "analytics")
public class AnalyticsModel {
    
    @Id
    private String id;
    private String accountId;
    private Set<AnalyticsProduct> products;
    private Date createdDate;

    public AnalyticsModel() {
    }

    public AnalyticsModel(final String accountId, final Set<AnalyticsProduct> products) {
        ArgumentVerifier.verifyNotNull(accountId);
        ArgumentVerifier.verifyNotEmpty(products);

        this.accountId = accountId;
        this.products = products;
        this.createdDate = new Date();
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

    public Set<AnalyticsProduct> getProducts() {
        return this.products;
    }

    public void setProducts(Set<AnalyticsProduct> products) {
        this.products = products;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public AnalyticsModel id(String id) {
        this.id = id;
        return this;
    }

    public AnalyticsModel accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public AnalyticsModel products(Set<AnalyticsProduct> products) {
        this.products = products;
        return this;
    }

    public AnalyticsModel createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AnalyticsModel)) {
            return false;
        }
        AnalyticsModel analitycsModel = (AnalyticsModel) o;
        return Objects.equals(id, analitycsModel.id) && 
                Objects.equals(accountId, analitycsModel.accountId) && 
                Objects.equals(products, analitycsModel.products) && 
                Objects.equals(createdDate, analitycsModel.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, products, createdDate);
    }

    @Override
    public String toString() {
        return "AnalitycsModel {" +
            " id='" + getId() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", products='" + getProducts() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
    
}
