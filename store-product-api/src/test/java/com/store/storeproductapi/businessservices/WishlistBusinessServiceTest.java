package com.store.storeproductapi.businessservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.services.AccountService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.services.WishlistService;

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
class WishlistBusinessServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ProductService productService;

    private WishlistBusinessService wishlistBusinessService;

    @BeforeEach
    void setup() {
        wishlistBusinessService = new WishlistBusinessService(this.wishlistService, this.accountService, this.productService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.wishlistService, this.accountService, this.productService);
    }

    @Test
    void createWishlist() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final AccountModel accountModel = PODAM_FACTORY.manufacturePojo(AccountModel.class);
        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);

        when(this.productService.findById(productModel.getId()))
                .thenReturn(productModel);
        when(this.accountService.findById(accountModel.getId()))
                .thenReturn(accountModel);
        when(this.wishlistService.createWishlist(accountModel.getId(), productModel.getId()))
                .thenReturn(wishlistModel);

        final Tuple2<WishlistModel, ProductModel> result = this.wishlistBusinessService.createWishlist(accountModel.getId(), 
                productModel.getId());

        assertThat(result).isNotNull();
        assertThat(result.getT1()).isEqualTo(wishlistModel);
        assertThat(result.getT2()).isEqualTo(productModel);

        verify(this.productService).findById(productModel.getId());
        verify(this.accountService).findById(accountModel.getId());
        verify(this.wishlistService).createWishlist(accountModel.getId(), productModel.getId());
    }

    @Test
    void addProductToWishlist() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final WishlistModel wishlistModel = PODAM_FACTORY.manufacturePojo(WishlistModel.class);

        when(this.productService.findById(productModel.getId()))
                .thenReturn(productModel);
        when(this.wishlistService.addProductToWishlist(wishlistModel.getId(), productModel.getId()))
                .thenReturn(wishlistModel);

        final Tuple2<WishlistModel, ProductModel> result = this.wishlistBusinessService.addProductToWishlist(wishlistModel.getId(), productModel.getId());

        assertThat(result).isNotNull();
        assertThat(result.getT1()).isEqualTo(wishlistModel);
        assertThat(result.getT2()).isEqualTo(productModel);

        verify(this.productService).findById(productModel.getId());
        verify(this.wishlistService).addProductToWishlist(wishlistModel.getId(), productModel.getId());
    }

}
