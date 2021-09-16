package com.store.storeproductapi.mappers;

import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.transferobjects.ProductTO;

public class ProductMapper {
    
    private ProductMapper() {

    }

    public static ProductTO mapRepoToProductTO(ProductModel productModel) {
        return new ProductTO()
            .id(productModel.getId())
            .title(productModel.getTitle())
            .price(productModel.getPrice())
            .description(productModel.getDescription())
            .imgUrl(productModel.getImgUrl())
            .quantity(productModel.getQuantity())
            .avgUserRating(productModel.getAvgUserRating())
            .numberOfVotes(productModel.getNumberOfVotes());
    }
    
}
