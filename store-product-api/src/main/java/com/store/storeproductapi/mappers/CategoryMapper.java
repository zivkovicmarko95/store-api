package com.store.storeproductapi.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.transferobjects.CategoryTO;
import com.store.storeproductapi.transferobjects.ProductTO;

public class CategoryMapper {
    
    private CategoryMapper() {
        
    }

    public static CategoryTO mapRepoToCategoryTO(final CategoryModel category, final Set<ProductModel> productModels) {

        return new CategoryTO()
            .id(category.getId())
            .description(category.getDescription())
            .title(category.getTitle())
            .products(mapRepoToProductTOs(productModels));
    }

    private static Set<ProductTO> mapRepoToProductTOs(Set<ProductModel> productModels) {

        return productModels.stream()
            .map(productModel -> ProductMapper.mapRepoToProductTO(productModel))
            .collect(Collectors.toSet());
    }

}
