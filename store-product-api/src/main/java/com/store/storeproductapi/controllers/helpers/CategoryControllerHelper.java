package com.store.storeproductapi.controllers.helpers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.CategoryBusinessService;
import com.store.storeproductapi.mappers.CategoryMapper;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.CategoryCreate;
import com.store.storeproductapi.services.CategoryService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.transferobjects.CategoryTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.util.function.Tuple2;

@Component
public class CategoryControllerHelper {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryControllerHelper.class);
    
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryBusinessService categoryBusinessService;

    @Autowired
    public CategoryControllerHelper(CategoryService categoryService, ProductService productService, 
            CategoryBusinessService categoryBusinessService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.categoryBusinessService = categoryBusinessService;
    }

    // path -> /categories/{category_id}
    public CategoryTO categoriesCategoryIdGet(final String categoryId) {
        
        final CategoryModel category = categoryService.findById(categoryId);
        final Set<String> productIds = category.getProductIds();
        
        final Set<ProductModel> products = productIds.stream()
                .map(productId -> this.productService.findById(productId))
                .collect(Collectors.toSet());

        return CategoryMapper.mapRepoToCategoryTO(category, products);
    }

    // path -> /categories
    public Set<CategoryTO> categoriesGet() {

        final List<CategoryModel> categories = this.categoryService.findAll();
        final Set<CategoryTO> categoryTOs = categories.stream()
                .map(category -> CategoryMapper.mapRepoToCategoryTO(category, Set.of()))
                .collect(Collectors.toSet());

        return categoryTOs;
    }

    // path -> internal/categories
    public CategoryTO internalCategoriesPost(final CategoryCreate categoryCreate) {

        final String title = categoryCreate.getTitle();
        final String description = categoryCreate.getDescription();
        
        final CategoryModel category = this.categoryService.createCategory(title, description);
        
        return CategoryMapper.mapRepoToCategoryTO(category, Set.of());
    }

    // path -> internal/categories/{category_id}/products/{product_id}
    public CategoryTO internalCategoriesCategoryIdProductsProductIdPost(final String categoryId, final String productId) {

        final Tuple2<CategoryModel, ProductModel> categoryProduct = categoryBusinessService.assignProductToCategory(productId, categoryId);
        final CategoryModel category = categoryProduct.getT1();
        final Set<String> productIds = category.getProductIds();

        final Set<ProductModel> products = productIds.stream()
                .map(prodId -> this.productService.findById(prodId))
                .collect(Collectors.toSet());

        return CategoryMapper.mapRepoToCategoryTO(category, products);
    }

    // path -> internal/categories/{category_id}/assign
    public CategoryTO internalCategoriesCategoryIdProductsAssignPost(final String categoryId, final Set<String> productIds) {

        final Tuple2<CategoryModel, Set<ProductModel>> categoryProducts = this.categoryBusinessService.assignProductsToCategory(productIds, categoryId);
        final CategoryModel category = categoryProducts.getT1();
        final Set<String> categoryProductIds = category.getProductIds();

        final Set<ProductModel> products = categoryProductIds.stream()
                .map(prodId -> this.productService.findById(prodId))
                .collect(Collectors.toSet());

        return CategoryMapper.mapRepoToCategoryTO(category, products);
    }

    // path -> internal/categories/{category_id}
    public DeleteResultTO internalCategoriesCategoryIdDelete(final String categoryId) {

        final CategoryModel categoryModel = this.categoryService.removeCategory(categoryId);

        LOGGER.info("Removing category {}", categoryModel);

        return new DeleteResultTO()
                .resourceId(categoryModel.getId())
                .message("Category has been removed");
    }

}
