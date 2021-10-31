package com.store.storeproductapi.controllers.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.CategoryBusinessService;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.CategoryCreate;
import com.store.storeproductapi.services.CategoryService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.transferobjects.CategoryTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.ProductTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ MockitoExtension.class, SpringExtension.class })
class CategoryControllerHelperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryBusinessService categoryBusinessService;

    private CategoryControllerHelper categoryControllerHelper;

    @BeforeEach
    void setup() {
        categoryControllerHelper = new CategoryControllerHelper(categoryService, productService, 
                categoryBusinessService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(categoryService, productService, categoryBusinessService);
    }

    @Test
    void categoriesCategoryIdGet() {

        final Set<ProductModel> products = PODAM_FACTORY.manufacturePojo(Set.class, ProductModel.class);
        final Set<String> productIds = products.stream()
                .map(product -> product.getId())
                .collect(Collectors.toSet());
        final CategoryModel categoryModel = PODAM_FACTORY.manufacturePojo(CategoryModel.class)
                .productIds(productIds);
        final String categoryId = categoryModel.getId();

        when(this.categoryService.findById(categoryId))
                .thenReturn(categoryModel);
        products.forEach(product -> {
            when(this.productService.findById(product.getId()))
                    .thenReturn(product);
        });

        final CategoryTO result = this.categoryControllerHelper.categoriesCategoryIdGet(categoryId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryModel.getId());
        assertThat(result.getDescription()).isEqualTo(categoryModel.getDescription());
        assertThat(result.getTitle()).isEqualTo(categoryModel.getTitle());

        final Set<String> resultProductIds = result.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toSet());

        assertThat(resultProductIds).containsAll(productIds);

        verify(this.categoryService).findById(categoryId);
        products.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }
    
    @Test
    void categoriesGet() {

        final List<CategoryModel> categories = PODAM_FACTORY.manufacturePojo(List.class, CategoryModel.class);

        when(this.categoryService.findAll())
                .thenReturn(categories);

        final Set<CategoryTO> result = this.categoryControllerHelper.categoriesGet();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSameSizeAs(categories);

        categories.forEach(category -> {

            final CategoryTO resultCategoryTO = result.stream()
                    .filter(categoryTO -> categoryTO.getId().equals(category.getId()))
                    .findFirst()
                    .get();

            assertThat(resultCategoryTO).isNotNull();
            assertThat(resultCategoryTO.getId()).isEqualTo(category.getId());
            assertThat(resultCategoryTO.getDescription()).isEqualTo(category.getDescription());
            assertThat(resultCategoryTO.getTitle()).isEqualTo(category.getTitle());
        });

        verify(this.categoryService).findAll();
    }

    @Test
    void internalCategoriesPost() {

        final CategoryCreate categoryCreate = PODAM_FACTORY.manufacturePojo(CategoryCreate.class);
        final String title = categoryCreate.getTitle();
        final String description = categoryCreate.getDescription();

        when(this.categoryService.createCategory(title, description))
                .thenReturn(new CategoryModel().description(description).title(title));

        final CategoryTO result = this.categoryControllerHelper.internalCategoriesPost(categoryCreate);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getTitle()).isEqualTo(title);

        verify(this.categoryService).createCategory(title, description);
    }

    @Test
    void internalCategoriesCategoryIdProductsProductIdPost() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final CategoryModel categoryModel = PODAM_FACTORY.manufacturePojo(CategoryModel.class)
                .productIds(Set.of(productModel.getId()));
        final Tuple2<CategoryModel, ProductModel> categoryProduct = Tuples.of(categoryModel, productModel);
        final String productId = productModel.getId();
        final String categoryId = categoryModel.getId();

        when(this.categoryBusinessService.assignProductToCategory(productId, categoryId))
                .thenReturn(categoryProduct);
        when(this.productService.findById(productId)).thenReturn(productModel);

        final CategoryTO result = this.categoryControllerHelper.internalCategoriesCategoryIdProductsProductIdPost(categoryId, productId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getDescription()).isEqualTo(categoryModel.getDescription());
        assertThat(result.getTitle()).isEqualTo(categoryModel.getTitle());
        
        final ProductTO resultProductTO = result.getProducts().stream()
                .findAny()
                .get();

        assertThat(resultProductTO.getId()).isEqualTo(productId);
        assertThat(resultProductTO.getDescription()).isEqualTo(productModel.getDescription());
        assertThat(resultProductTO.getImgUrl()).isEqualTo(productModel.getImgUrl());
        assertThat(resultProductTO.getDescription()).isEqualTo(productModel.getDescription());
        assertThat(resultProductTO.getNumberOfVotes()).isEqualTo(productModel.getNumberOfVotes());
        assertThat(resultProductTO.getPrice()).isEqualTo(productModel.getPrice());
        assertThat(resultProductTO.getQuantity()).isEqualTo(productModel.getQuantity());
        assertThat(resultProductTO.getTitle()).isEqualTo(productModel.getTitle());

        verify(this.categoryBusinessService).assignProductToCategory(productId, categoryId);
        verify(this.productService).findById(productId);
    }

    @Test
    void internalCategoriesCategoryIdProductsAssignPost() {

        final Set<ProductModel> products = PODAM_FACTORY.manufacturePojo(Set.class, ProductModel.class);
        final Set<String> productIds = products.stream()
                .map(product -> product.getId())
                .collect(Collectors.toSet());
        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class)
                .productIds(productIds);
        final String categoryId = category.getId();

        final Tuple2<CategoryModel, Set<ProductModel>> categoryProducts = Tuples.of(category, products);

        when(this.categoryBusinessService.assignProductsToCategory(productIds, categoryId))
                .thenReturn(categoryProducts);
        products.forEach(product -> {
            when(this.productService.findById(product.getId()))
                .thenReturn(product);
        });

        final CategoryTO result = this.categoryControllerHelper.internalCategoriesCategoryIdProductsAssignPost(categoryId, productIds);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getDescription()).isEqualTo(category.getDescription());
        assertThat(result.getTitle()).isEqualTo(category.getTitle());

        result.getProducts().forEach(productTO -> {

            final ProductModel product = products.stream()
                    .filter(prod -> prod.getId().equals(productTO.getId()))
                    .findFirst()
                    .get();

            assertThat(productTO.getId()).isEqualTo(product.getId());
            assertThat(productTO.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
            assertThat(productTO.getDescription()).isEqualTo(product.getDescription());
            assertThat(productTO.getImgUrl()).isEqualTo(product.getImgUrl());
            assertThat(productTO.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
            assertThat(productTO.getPrice()).isEqualTo(product.getPrice());
            assertThat(productTO.getQuantity()).isEqualTo(product.getQuantity());
            assertThat(productTO.getTitle()).isEqualTo(product.getTitle());
        });

        verify(this.categoryBusinessService).assignProductsToCategory(productIds, categoryId);
        products.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }

    @Test
    void internalCategoriesCategoryIdDelete() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String categoryId = category.getId();

        when(this.categoryService.removeCategory(categoryId))
                .thenReturn(category);

        final DeleteResultTO result = this.categoryControllerHelper.internalCategoriesCategoryIdDelete(categoryId);

        assertThat(result.getResourceId()).isEqualTo(categoryId);
        assertThat(result.getMessage()).isEqualTo("Category has been removed");

        verify(this.categoryService).removeCategory(categoryId);
    }

}
