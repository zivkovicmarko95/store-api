package com.store.storeproductapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.store.storeproductapi.exceptions.ResourceExistException;
import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.services.impl.ProductServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class ProductServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductServiceImpl(productRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findById() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String id = product.getId();

        when(this.productRepository.findById(id)).thenReturn(Optional.of(product));

        final ProductModel result = this.productService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(product);
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getTitle()).isEqualTo(product.getTitle());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(result.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
        assertThat(result.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
        assertThat(result.getVisible()).isEqualTo(product.getVisible());

        verify(this.productRepository).findById(id);
    }

    @Test
    void findById_productNotFound() {
        
        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Product with provided id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.productRepository).findById(id);
    }

    @Test
    void findByTitle() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String title = product.getTitle();

        when(this.productRepository.findByTitle(title)).thenReturn(Optional.of(product));

        final ProductModel result = this.productService.findByTitle(title);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(product);
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getTitle()).isEqualTo(product.getTitle());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(product.getImgUrl());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(result.getAvgUserRating()).isEqualTo(product.getAvgUserRating());
        assertThat(result.getNumberOfVotes()).isEqualTo(product.getNumberOfVotes());
        assertThat(result.getVisible()).isEqualTo(product.getVisible());

        verify(this.productRepository).findByTitle(title);
    }

    @Test
    void findByTitle_productNotFound() {

        final String title = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.productRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findByTitle(title))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Product with provided title %s is not found",
                    title
                )
            )
            .hasNoCause();

        verify(this.productRepository).findByTitle(title);
    }

    @Test
    void findAll() {

        final List<ProductModel> products = PODAM_FACTORY.manufacturePojo(List.class, ProductModel.class);
        final Page<ProductModel> productPages = new PageImpl<>(products);
        final int page = PODAM_FACTORY.manufacturePojo(Integer.class);
        final PageRequest pageRequest = PageRequest.of(page, 48);

        when(this.productRepository.findAll(pageRequest)).thenReturn(productPages);

        final Page<ProductModel> result = this.productService.findAll(page);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(productPages);
        assertThat(result).hasSameElementsAs(productPages);

        verify(this.productRepository).findAll(pageRequest);
    }

    @Test
    void createProduct() {

        final String title = PODAM_FACTORY.manufacturePojo(String.class);
        final float price = PODAM_FACTORY.manufacturePojo(Float.class);
        final String description = PODAM_FACTORY.manufacturePojo(String.class);
        final String imgUrl = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);

        final ProductModel product = new ProductModel()
                .title(title)
                .price(price)
                .description(description)
                .imgUrl(imgUrl)
                .quantity(quantity);

        when(this.productRepository.findOne(any())).thenReturn(Optional.empty());
        when(this.productRepository.insert(product)).thenReturn(product);

        final ProductModel result = this.productService.createProduct(title, price, description, imgUrl, quantity);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(product);

        verify(this.productRepository).findOne(any());
        verify(this.productRepository).insert(product);
    }

    @Test
    void createProduct_productExists() {

        final String title = PODAM_FACTORY.manufacturePojo(String.class);
        final float price = PODAM_FACTORY.manufacturePojo(Float.class);
        final String description = PODAM_FACTORY.manufacturePojo(String.class);
        final String imgUrl = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);
        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);

        when(this.productRepository.findOne(any())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> this.productService.createProduct(title, price, description, imgUrl, quantity))
            .isExactlyInstanceOf(ResourceExistException.class)
            .hasMessageStartingWith(
                String.format(
                    "Product with provided title %s exists",
                    title
                )
            )
            .hasNoCause();

        verify(this.productRepository).findOne(any());
    }

    @Test
    void updateProductQuantity() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class)
            .quantity(100);
        final String productId = product.getId();
        final int orderedQuantity = 10;

        when(this.productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(this.productRepository.save(product)).thenReturn(product);

        final ProductModel result = this.productService.updateProductQuantity(productId, orderedQuantity);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(product);
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());

        verify(this.productRepository).findById(productId);
        verify(this.productRepository).save(product);
    }

    @Test
    void updateProductQuantity_productNotFound() {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);

        when(this.productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.updateProductQuantity(productId, quantity))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Product with provided id %s is not found",
                    productId
                )
            )
            .hasNoCause();

        verify(this.productRepository).findById(productId);
    }

    @Test
    void updateProductQuantity_productQuantityLowerThenExpected() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class)
            .quantity(10);
        final String productId = product.getId();
        final int orderedQuantity = 100;

        when(this.productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> this.productService.updateProductQuantity(productId, orderedQuantity))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Updating quanity of the product with product ID %s is not possible, because quanity %d is more then available products.",
                    product,
                    orderedQuantity
                )
            )
            .hasNoCause();

        verify(this.productRepository).findById(productId);
    }

    @Test
    void removeProduct() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final String id = product.getId();

        when(this.productRepository.findById(id)).thenReturn(Optional.of(product));
        when(this.productRepository.save(product)).thenReturn(product);

        final ProductModel result = this.productService.removeProduct(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getVisible()).isFalse();
        assertThat(result).isEqualTo(product);

        verify(this.productRepository).findById(id);
        verify(this.productRepository).save(product);
    }

    @Test
    void removeProduct_productNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.removeProduct(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Product with provided id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.productRepository).findById(id);
    }

    @Test
    void hardRemoveProduct() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class)
            .visible(false);
        final String id = product.getId();
        
        when(this.productRepository.findById(id)).thenReturn(Optional.of(product));

        this.productService.hardRemoveProduct(id);

        verify(this.productRepository).findById(id);
        verify(this.productRepository).delete(product);
    }

    @Test
    void hardRemoveProduct_productNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.hardRemoveProduct(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Product with provided id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.productRepository).findById(id);
    }

    @Test
    void hardRemoveProduct_productVisible() {

        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class)
            .visible(true);
        final String id = product.getId();

        when(this.productRepository.findById(id)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> this.productService.hardRemoveProduct(id))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Removing product with id %s is not possible. Product: %s",
                    id,
                    product
                )
            )
            .hasNoCause();

        verify(this.productRepository).findById(id);
    }

}
