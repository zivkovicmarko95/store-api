package com.store.storeproductapi.controllers.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.store.storeproductapi.businessservices.ProductInventoryBusinessService;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.Product;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.ProductTO;

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
class ProductControllerHelperTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductInventoryBusinessService productInventoryBusinessService;

    private ProductControllerHelper productControllerHelper;

    @BeforeEach
    void setup() {
        productControllerHelper = new ProductControllerHelper(productService, productInventoryBusinessService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(productService, productInventoryBusinessService);
    }

    @Test
    void productsProductIdAndTitleGet() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String productId = product.getId();
        final String productTitle = product.getTitle();

        when(this.productService.findById(productId))
                .thenReturn(product);
        when(this.productService.findByTitle(productTitle))
                .thenReturn(product);

        final ProductTO result = this.productControllerHelper.productsProductIdAndTitleGet(productId, productTitle);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getTitle()).isEqualTo(productTitle);
        assertThat(result.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());

        verify(this.productService).findById(productId);
        verify(this.productService).findByTitle(productTitle);
        verify(this.productInventoryBusinessService).verifyProductsQuantity(productId);
    }

    @Test
    void productsProductIdAndTitleGet_noProductId() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String productTitle = product.getTitle();

        when(this.productService.findByTitle(productTitle))
                .thenReturn(product);

        final ProductTO result = this.productControllerHelper.productsProductIdAndTitleGet(null, productTitle);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getTitle()).isEqualTo(productTitle);
        assertThat(result.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());

        verify(this.productService).findByTitle(productTitle);
        verify(this.productInventoryBusinessService).verifyProductsQuantity(product.getId());
    }

    @Test
    void productsProductIdAndTitleGet_noTitle() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String productId = product.getId();

        when(this.productService.findById(productId))
                .thenReturn(product);

        final ProductTO result = this.productControllerHelper.productsProductIdAndTitleGet(productId, null);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getTitle()).isEqualTo(product.getTitle());
        assertThat(result.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());

        verify(this.productService).findById(productId);
        verify(this.productInventoryBusinessService).verifyProductsQuantity(product.getId());
    }

    @Test
    void internalProductsPost() {

        final Product product = PODAM_FACTORY.manufacturePojo(Product.class);
        final String title = product.getTitle();
        final float price = product.getPrice();
        final String description = product.getDescription();
        final String imgUrl = product.getImgUrl();
        final int quantity = product.getQuantity();
        final ProductModel productModel = 
                new ProductModel(title, price, description, imgUrl, quantity, 0, 0, true);

        when(this.productService.createProduct(title, price, description, imgUrl, quantity))
                .thenReturn(productModel);

        final ProductTO result = productControllerHelper.internalProductsPost(product);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getPrice()).isEqualTo(price);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getImgUrl()).isEqualTo(imgUrl);
        assertThat(result.getQuantity()).isEqualTo(quantity);

        verify(this.productService).createProduct(title, price, description, imgUrl, quantity);
    }

    @Test
    void internalProductsProductIdDelete() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String productId = product.getId();

        when(this.productService.removeProduct(productId))
            .thenReturn(product);

        final DeleteResultTO result = this.productControllerHelper.internalProductsProductIdDelete(productId);

        assertThat(result).isNotNull();
        assertThat(result.getResourceId()).isEqualTo(productId);
        assertThat(result.getMessage()).isEqualTo("Product has been removed.");

        verify(this.productService).removeProduct(productId);
    }

    @Test
    void internalProductsProductIdRemoveDelete() {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        final DeleteResultTO result = this.productControllerHelper.internalProductsProductIdRemoveDelete(productId);

        assertThat(result).isNotNull();
        assertThat(result.getResourceId()).isEqualTo(productId);
        assertThat(result.getMessage()).isEqualTo("Product has been removed from the DB.");
        
        verify(this.productService).hardRemoveProduct(productId);
    }

}
