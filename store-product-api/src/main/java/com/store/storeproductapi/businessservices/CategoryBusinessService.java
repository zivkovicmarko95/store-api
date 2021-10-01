package com.store.storeproductapi.businessservices;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.services.CategoryService;
import com.store.storeproductapi.services.ProductService;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class CategoryBusinessService {
    
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public CategoryBusinessService(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /**
     * Method that assigns product to the particular category
     * 
     * @param productId ID of the product
     * @param categoryId ID of the category
     * @return Tuples of {@link CategoryModel} and {@link ProductModel}
     */
    public Tuple2<CategoryModel, ProductModel> assignProductToCategory(final String productId, final String categoryId) {
        ArgumentVerifier.verifyNotNull(productId, categoryId);
        
        final ProductModel product = productService.findById(productId);
        final CategoryModel category = categoryService.assignProductToCategory(productId, categoryId);

        return Tuples.of(category, product);
    }

    /**
     * Method that assigns products to the particular category
     * 
     * @param productIds IDs of the products
     * @param categoryId ID of the category
     * @return Tuples of {@link CategoryModel} and set of {@link ProductModel}
     */
    public Tuple2<CategoryModel, Set<ProductModel>> assignProductsToCategory(final Set<String> productIds, final String categoryId) {
        ArgumentVerifier.verifyNotNull(categoryId);

        final Set<ProductModel> products = productIds.stream()
                .map(productId -> productService.findById(productId))
                .collect(Collectors.toSet());

        final CategoryModel category = categoryService.assignProductsToCategory(productIds, categoryId);

        return Tuples.of(category, products);
    }

}
