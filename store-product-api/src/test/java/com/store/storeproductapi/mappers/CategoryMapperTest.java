package com.store.storeproductapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.transferobjects.CategoryTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class CategoryMapperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToCategoryTO() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final Set<ProductModel> products = generateProducts(category.getProductIds());

        final CategoryTO result = CategoryMapper.mapRepoToCategoryTO(category, products);

        result.getProducts().forEach(productTO -> {

            final String id = productTO.getId();

            assertThat(category.getProductIds()).contains(id);
        });
    }

    private Set<ProductModel> generateProducts(Set<String> productIds) {
        return productIds.stream().map(productId -> {
            return PODAM_FACTORY.manufacturePojo(ProductModel.class)
                .id(productId)
                .visible(true);
        }).collect(Collectors.toSet());
    }

}
