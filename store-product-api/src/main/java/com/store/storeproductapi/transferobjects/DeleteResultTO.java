package com.store.storeproductapi.transferobjects;

import java.util.Objects;

/**
 * Transfer object used when the resource is deleted
 */
public class DeleteResultTO {
    
    private String resourceId;
    private String message;

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DeleteResultTO resourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public DeleteResultTO message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DeleteResultTO)) {
            return false;
        }
        DeleteResultTO deleteResultTO = (DeleteResultTO) o;
        return Objects.equals(resourceId, deleteResultTO.resourceId) && 
                Objects.equals(message, deleteResultTO.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, message);
    }

    @Override
    public String toString() {
        return "{" +
            " resourceId='" + getResourceId() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
    
}
