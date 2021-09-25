package com.store.storeproductapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.transferobjects.ProductTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class ProductMapperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToProductTO() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);

        final ProductTO result = ProductMapper.mapRepoToProductTO(product);

        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getTitle()).isEqualTo(product.getTitle());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(result.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
        assertThat(result.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
    }

}
