package com.store.storeproductapi.businessservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.services.CategoryService;
import com.store.storeproductapi.services.ProductService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.util.function.Tuple2;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class CategoryBusinessServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    private CategoryBusinessService categoryBusinessService;

    @BeforeEach
    void setup() {
        categoryBusinessService = new CategoryBusinessService(categoryService, productService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(categoryService, productService);
    }

    @Test
    void assignProductToCategory() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);

        final String productId = product.getId();
        final String categoryId = category.getId();

        when(this.productService.findById(productId)).thenReturn(product);
        when(this.categoryService.assignProductToCategory(productId, categoryId)).thenReturn(category);

        final Tuple2<CategoryModel, ProductModel> result = this.categoryBusinessService.assignProductToCategory(productId, categoryId);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.getT1()).isEqualTo(category);
        assertThat(result.getT2()).isEqualTo(product);

        verify(this.productService).findById(productId);
        verify(this.categoryService).assignProductToCategory(productId, categoryId);
    }

    @Test
    void assignProductToCategory_productNotFound() {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final String categoryId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.productService.findById(productId)).thenThrow(StoreResourceNotFoundException.class);

        assertThatThrownBy(() -> this.categoryBusinessService.assignProductToCategory(productId, categoryId))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasNoCause();

        verify(this.productService).findById(productId);
    }

    @Test
    void assignProductToCategory_productAlreadyExist() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String categoryId = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = product.getId();

        when(this.productService.findById(productId)).thenReturn(product);
        when(this.categoryService.assignProductToCategory(productId, categoryId)).thenThrow(ResourceStateException.class);

        assertThatThrownBy(() -> this.categoryBusinessService.assignProductToCategory(productId, categoryId))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasNoCause();

        verify(this.productService).findById(productId);
        verify(this.categoryService).assignProductToCategory(productId, categoryId);
    }

    @Test
    void assignProductsToCategory() {
        
        final Set<ProductModel> products = PODAM_FACTORY.manufacturePojo(Set.class, ProductModel.class);
        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);

        final String categoryId = category.getId();
        final Set<String> productIds = products.stream()
                .map(ProductModel::getId)
                .collect(Collectors.toSet());

        products.forEach(product -> {
            when(this.productService.findById(product.getId())).thenReturn(product);
        });
        when(this.categoryService.assignProductsToCategory(productIds, categoryId)).thenReturn(category);

        final Tuple2<CategoryModel, Set<ProductModel>> result = this.categoryBusinessService.assignProductsToCategory(productIds, categoryId);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.getT1()).isEqualTo(category);
        assertThat(result.getT2()).isEqualTo(products);

        productIds.forEach(productId -> {
            verify(this.productService).findById(productId);
        });
        verify(this.categoryService).assignProductsToCategory(productIds, categoryId);
    }

    @Test
    void assignProductsToCategory_productNotFound() {

        final Set<String> productIds = PODAM_FACTORY.manufacturePojo(Set.class, String.class);
        final String productId = productIds.stream()
                .findFirst().get();
        final String categoryId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.productService.findById(productId)).thenThrow(StoreResourceNotFoundException.class);

        assertThatThrownBy(() -> this.categoryBusinessService.assignProductsToCategory(productIds, categoryId))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasNoCause();

        verify(this.productService).findById(productId);
    }

    @Test
    void assignProductsToCategory_productExists() {

        final Set<ProductModel> products = PODAM_FACTORY.manufacturePojo(Set.class, ProductModel.class);
        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);

        final String categoryId = category.getId();
        final Set<String> productIds = products.stream()
                .map(ProductModel::getId)
                .collect(Collectors.toSet());

        products.forEach(product -> {
            when(this.productService.findById(product.getId())).thenReturn(product);
        });
        when(this.categoryService.assignProductsToCategory(productIds, categoryId)).thenThrow(ResourceStateException.class);

        assertThatThrownBy(() -> this.categoryBusinessService.assignProductsToCategory(productIds, categoryId))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasNoCause();

        productIds.forEach(productId -> {
            verify(this.productService).findById(productId);
        });
        verify(this.categoryService).assignProductsToCategory(productIds, categoryId);
    }

}
