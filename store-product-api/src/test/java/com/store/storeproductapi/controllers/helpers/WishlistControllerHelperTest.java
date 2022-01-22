package com.store.storeproductapi.controllers.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.WishlistBusinessService;
import com.store.storeproductapi.mappers.ProductMapper;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.services.WishlistService;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.WishlistTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.util.function.Tuples;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ MockitoExtension.class, SpringExtension.class })
class WishlistControllerHelperTest {
    
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private WishlistBusinessService wishlistBusinessService;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private ProductService productService;

    private WishlistControllerHelper wishlistControllerHelper;

    @BeforeEach
    void setup() {
        wishlistControllerHelper = new WishlistControllerHelper(wishlistBusinessService, wishlistService, productService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.wishlistBusinessService, this.wishlistService, this.productService);
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet() {

        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final Set<ProductModel> productModels = generateProducts(wishlistModel.getProductIds());
        final String wishlistId = wishlistModel.getId();
        final String accountId = wishlistModel.getAccountId();

        when(this.wishlistService.findById(wishlistId))
                .thenReturn(wishlistModel);
        when(this.wishlistService.findByAccountId(accountId))
                .thenReturn(wishlistModel);
        productModels.forEach(product -> {
            when(this.productService.findById(product.getId()))
                    .thenReturn(product);
        });

        final WishlistTO result = wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(wishlistId, accountId);

        assertThat(result.getId()).isEqualTo(wishlistId);
        result.getProducts().stream().forEach(productTO -> {
            
            final ProductModel foundProduct = productModels.stream()
                    .filter(product -> product.getId().equals(productTO.getId()))
                    .findFirst().get();

            assertThat(productTO).isEqualTo(ProductMapper.mapRepoToProductTO(foundProduct));
        });

        verify(this.wishlistService).findById(wishlistId);
        verify(this.wishlistService).findByAccountId(accountId);
        productModels.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_wishlistId_null() {

        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String wishlistId = wishlistModel.getId();
        final String accountId = wishlistModel.getAccountId();
        final Set<ProductModel> productModels = generateProducts(wishlistModel.getProductIds());

        when(this.wishlistService.findByAccountId(accountId))
                .thenReturn(wishlistModel);
        productModels.forEach(product -> {
            when(this.productService.findById(product.getId()))
                    .thenReturn(product);
        });

        final WishlistTO result = wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(null, accountId);

        assertThat(result.getId()).isEqualTo(wishlistId);
        result.getProducts().stream().forEach(productTO -> {
            
            final ProductModel foundProduct = productModels.stream()
                    .filter(product -> product.getId().equals(productTO.getId()))
                    .findFirst().get();

            assertThat(productTO).isEqualTo(ProductMapper.mapRepoToProductTO(foundProduct));
        });

        verify(this.wishlistService).findByAccountId(accountId);
        productModels.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }

    @Test
    void wishlistsWishlistIdAndAccountIdGet_accountId_null() {

        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String wishlistId = wishlistModel.getId();
        final Set<ProductModel> productModels = generateProducts(wishlistModel.getProductIds());

        when(this.wishlistService.findById(wishlistId))
                .thenReturn(wishlistModel);
        productModels.forEach(product -> {
            when(this.productService.findById(product.getId()))
                    .thenReturn(product);
        });

        final WishlistTO result = wishlistControllerHelper.wishlistsWishlistIdAndAccountIdGet(wishlistId, null);

        assertThat(result.getId()).isEqualTo(wishlistId);
        result.getProducts().stream().forEach(productTO -> {
            
            final ProductModel foundProduct = productModels.stream()
                    .filter(product -> product.getId().equals(productTO.getId()))
                    .findFirst().get();

            assertThat(productTO).isEqualTo(ProductMapper.mapRepoToProductTO(foundProduct));
        });

        verify(this.wishlistService).findById(wishlistId);
        productModels.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }

    @Test
    void wishlistsPost() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class)
                .productIds(Set.of(productModel.getId()));
        final String accountId = wishlistModel.getAccountId();

        when(this.wishlistBusinessService.createWishlist(accountId, productModel.getId()))
            .thenReturn(Tuples.of(wishlistModel, productModel));

        final WishlistTO result = this.wishlistControllerHelper.wishlistsPost(accountId, productModel.getId());

        assertThat(result.getId()).isEqualTo(wishlistModel.getId());
        assertThat(result.getProducts().stream().findAny().get())
                .isEqualTo(ProductMapper.mapRepoToProductTO(productModel));

        verify(this.wishlistBusinessService).createWishlist(accountId, productModel.getId());
    }

    @Test
    void wishlistsWishlistIdAddProductIdPost() {        
        
        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final Set<ProductModel> productModels = generateProducts(wishlistModel.getProductIds());
        final ProductModel product = productModels.stream().findAny()
                .get();

        when(this.wishlistBusinessService.addProductToWishlist(wishlistModel.getId(), product.getId()))
                .thenReturn(Tuples.of(wishlistModel, product));
        productModels.forEach(productModel -> {
            when(this.productService.findById(productModel.getId())).thenReturn(productModel);
        });

        final WishlistTO result = wishlistControllerHelper.wishlistsWishlistIdAddProductIdPost(wishlistModel.getId(), product.getId());

        assertThat(result.getId()).isEqualTo(wishlistModel.getId());
        result.getProducts().forEach(productTO -> {

            final ProductModel foundProduct = productModels.stream().filter(p -> p.getId().equals(productTO.getId()))
                    .findFirst().get();

            assertThat(productTO).isEqualTo(ProductMapper.mapRepoToProductTO(foundProduct));
        });

        verify(this.wishlistBusinessService).addProductToWishlist(wishlistModel.getId(), product.getId());
        productModels.forEach(productModel -> verify(this.productService).findById(productModel.getId()));
    }

    @Test
    void wishlistsWishlistIdRemoveProductIdDelete() {

        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final Set<ProductModel> productModels = generateProducts(wishlistModel.getProductIds());
        final ProductModel product = productModels.stream().findAny()
                .get();

        when(this.wishlistService.removeProductFromWishlist(wishlistModel.getId(), product.getId()))
                .thenReturn(wishlistModel);
        productModels.forEach(productModel -> {
            when(this.productService.findById(productModel.getId())).thenReturn(productModel);
        });

        final WishlistTO result = wishlistControllerHelper.wishlistsWishlistIdRemoveProductIdDelete(wishlistModel.getId(), product.getId());

        assertThat(result.getId()).isEqualTo(wishlistModel.getId());
        result.getProducts().forEach(productTO -> {

            final ProductModel foundProduct = productModels.stream().filter(p -> p.getId().equals(productTO.getId()))
                    .findFirst().get();

            assertThat(productTO).isEqualTo(ProductMapper.mapRepoToProductTO(foundProduct));
        });

        verify(this.wishlistService).removeProductFromWishlist(wishlistModel.getId(), product.getId());
        productModels.forEach(productModel -> verify(this.productService).findById(productModel.getId()));
    }

    @Test
    void wishlistsWishlistIdDelete() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        final DeleteResultTO result = this.wishlistControllerHelper.wishlistsWishlistIdDelete(id);

        assertThat(result.getResourceId()).isEqualTo(id);
        assertThat(result.getMessage()).isEqualTo(String.format("Wishlist with id %s is removed", id));

        verify(this.wishlistService).deleteWishlistById(id);
    }

    private Set<ProductModel> generateProducts(final Set<String> productIds) {
        return productIds.stream()
            .map(productId -> PODAM_FACTORY.manufacturePojo(ProductModel.class).id(productId))
            .collect(Collectors.toSet());
    }
}
