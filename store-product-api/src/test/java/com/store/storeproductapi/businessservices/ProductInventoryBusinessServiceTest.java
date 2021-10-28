package com.store.storeproductapi.businessservices;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.services.ProductService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ MockitoExtension.class, SpringExtension.class })
class ProductInventoryBusinessServiceTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private ProductService productService;

    private ProductInventoryBusinessService productInventoryBusinessService;

    @BeforeEach
    void setup() {
        productInventoryBusinessService = new ProductInventoryBusinessService(productService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(productService);
    }

    @Test
    void verifyProductsQuantity() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class)
                .visible(true);
        final String id = productModel.getId();

        when(this.productService.findById(id))
                .thenReturn(productModel);

        this.productInventoryBusinessService.verifyProductsQuantity(id);

        verify(this.productService).findById(id);
    }

    @Test
    void verifyNoMoreInteractions_notVisible() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class)
                .visible(false);
        final String id = productModel.getId();

        when(this.productService.findById(id))
                .thenReturn(productModel);

        assertThatThrownBy(() -> this.productInventoryBusinessService.verifyProductsQuantity(id))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasNoCause();

        verify(this.productService).findById(id);
    }

    @Test
    void verifyNoMoreInteractions_quantityZero() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class)
                .quantity(0);
        final String id = productModel.getId();

        when(this.productService.findById(id))
                .thenReturn(productModel);

        assertThatThrownBy(() -> this.productInventoryBusinessService.verifyProductsQuantity(id))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasNoCause();

        verify(this.productService).findById(id);
    }

}
