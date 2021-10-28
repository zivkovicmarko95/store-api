package com.store.storeproductapi.controllers.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.CartBusinessService;
import com.store.storeproductapi.businessservices.ProductInventoryBusinessService;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.Cart;
import com.store.storeproductapi.models.api.CartCreate;
import com.store.storeproductapi.services.CartService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.transferobjects.CartTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

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

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class CartControllerHelperTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private CartBusinessService cartBusinessService;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductInventoryBusinessService productInventoryBusinessService;

    private CartControllerHelper cartControllerHelper;

    @BeforeEach
    void setup() {
        cartControllerHelper = new CartControllerHelper(cartBusinessService, cartService, 
                productService, productInventoryBusinessService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(cartBusinessService, cartService, productService, productInventoryBusinessService);
    }

    @Test
    void cartsCartIdGet() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final Set<ProductModel> productModels = cartModel.getCartProducts().stream()
                .map(cartProduct -> PODAM_FACTORY.manufacturePojo(ProductModel.class).id(cartProduct.getProductId()))
                .collect(Collectors.toSet());
        final String cartId = cartModel.getId();
            
        when(this.cartService.findById(cartId)).thenReturn(cartModel);
        
        productModels.forEach(productModel -> {
            when(this.productService.findById(productModel.getId())).thenReturn(productModel);
        });
        
        final CartTO result = cartControllerHelper.cartsCartIdGet(cartId);

        assertThat(result.getId()).isEqualTo(cartModel.getId());

        verify(this.cartService).findById(cartId);

        productModels.forEach(productModel -> {
            verify(this.productInventoryBusinessService).verifyProductsQuantity(productModel.getId());
            verify(this.productService).findById(productModel.getId());
        });
    }

    @Test
    void cartsPost() {

        final ProductModel productModel = PODAM_FACTORY.manufacturePojo(ProductModel.class);
        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class)
                .cartProducts(Set.of(new CartProductModel(productModel.getId(), 10)));
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = 10;

        final CartCreate cartCreate = new CartCreate(productModel.getId(), accountId, quantity);

        when(this.cartBusinessService.createCartAndAddProduct(productModel.getId(), accountId, quantity))
                .thenReturn(cartModel);
        when(this.productService.findById(productModel.getId()))
                .thenReturn(productModel);

        final CartTO result = this.cartControllerHelper.cartsPost(cartCreate);

        assertThat(result.getId()).isEqualTo(cartModel.getId());
        assertThat(result.getCartProducts().stream().findAny().get().getId())
                .isEqualTo(cartModel.getCartProducts().stream().findAny().get().getProductId());

        verify(this.cartBusinessService).createCartAndAddProduct(productModel.getId(), accountId, quantity);
        verify(this.productInventoryBusinessService).verifyProductsQuantity(productModel.getId());
        verify(this.productService).findById(productModel.getId());
    }

    @Test
    void cartsAddPost() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final Set<ProductModel> productModels = cartModel.getCartProducts().stream()
                .map(cartProduct -> PODAM_FACTORY.manufacturePojo(ProductModel.class).id(cartProduct.getProductId()))
                .collect(Collectors.toSet());

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final ProductModel productModel = productModels.stream()
                .findAny().get();
        final int quantity = 10;

        final Cart cart = new Cart(cartModel.getId(), productModel.getId(), accountId, quantity);
            
        when(this.cartBusinessService.addProductToCart(cartModel.getId(), productModel.getId(), accountId, quantity))
            .thenReturn(Tuples.of(cartModel, productModel));
        productModels.forEach(product -> {
            when(this.productService.findById(product.getId())).thenReturn(product);
        });

        final CartTO result = this.cartControllerHelper.cartsAddPost(cart);
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cart.getCartId());

        verify(this.cartBusinessService).addProductToCart(cartModel.getId(), productModel.getId(), accountId, quantity);
        verify(this.productInventoryBusinessService).verifyProductsQuantity(productModel.getId());
        productModels.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }

    @Test
    void cartsRemoveDelete() {

        final Cart cart = PODAM_FACTORY.manufacturePojo(Cart.class);
        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class)
                .id(cart.getCartId());
        final Set<ProductModel> productModels = cartModel.getCartProducts().stream()
                .map(cartProduct -> PODAM_FACTORY.manufacturePojo(ProductModel.class).id(cartProduct.getProductId()))
                .collect(Collectors.toSet());

        when(this.cartService.findById(cartModel.getId()))
                .thenReturn(cartModel);
        productModels.forEach(product -> {
            when(this.productService.findById(product.getId()))
                    .thenReturn(product);
        });

        final CartTO result = this.cartControllerHelper.cartsRemoveDelete(cart);

        assertThat(result.getId()).isEqualTo(cartModel.getId());

        verify(this.cartBusinessService).removeProductFromCart(cartModel.getId(), cart.getAccountId(), cart.getProductId());
        verify(this.cartService).findById(cartModel.getId());
        productModels.forEach(product -> {
            verify(this.productService).findById(product.getId());
        });
    }

    @Test
    void cartsRemoveDelete_removeCart() {

        final Cart cart = PODAM_FACTORY.manufacturePojo(Cart.class);
        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class)
                .id(cart.getCartId());

        when(this.cartService.findById(cartModel.getId()))
                .thenThrow(StoreGeneralException.class);

        final CartTO result = this.cartControllerHelper.cartsRemoveDelete(cart);

        assertThat(result.getId()).isNull();
        assertThat(result.getCartProducts()).isNull();

        verify(this.cartBusinessService).removeProductFromCart(cartModel.getId(), cart.getAccountId(), cart.getProductId());
        verify(this.cartService).findById(cartModel.getId());
    }

    @Test
    void internalCartsCartIdDelete() {

        final String cartId = PODAM_FACTORY.manufacturePojo(String.class);
        final DeleteResultTO deleteResultTO = new DeleteResultTO()
                .resourceId(cartId)
                .message("Cart has been removed.");

        final DeleteResultTO result = this.cartControllerHelper.internalCartsCartIdDelete(cartId);

        assertThat(result.getResourceId()).isNotNull();
        assertThat(result.getResourceId()).isEqualTo(cartId);
        assertThat(result.getMessage()).isEqualTo("Cart has been removed.");
        assertThat(result).isEqualTo(deleteResultTO);

        verify(this.cartBusinessService).removeCart(cartId);
    }

}
