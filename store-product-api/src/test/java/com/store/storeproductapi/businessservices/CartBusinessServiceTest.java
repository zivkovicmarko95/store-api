package com.store.storeproductapi.businessservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;

import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.services.CartService;
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

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class CartBusinessServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    private CartBusinessService cartBusinessService;

    @BeforeEach
    void setup() {
        cartBusinessService = new CartBusinessService(cartService, productService);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(cartService, productService);
    }

    @Test
    void createCartAndAddProduct() {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);
        final ProductModel product = PODAM_FACTORY.manufacturePojo(ProductModel.class)
            .id(productId)
            .quantity(quantity);
        final CartModel cart = PODAM_FACTORY.manufacturePojo(CartModel.class)
            .cartProducts(Set.of(new CartProductModel(productId, quantity)));
        final String accountId = cart.getAccountId();
        
        when(this.productService.updateProductQuantity(productId, quantity))
            .thenReturn(product);
        when(this.cartService.createCart(productId, accountId, quantity))
            .thenReturn(cart);

        final CartModel result = this.cartBusinessService.createCartAndAddProduct(productId, accountId, quantity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cart.getId());
        assertThat(result).isEqualTo(cart);

        verify(this.productService).updateProductQuantity(productId, quantity);
        verify(this.cartService).createCart(productId, accountId, quantity);
    }

    @Test
    void removeProductFromCart() {

        final CartModel cart = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String cartId = cart.getId();
        final String accountId = cart.getAccountId();
        final CartProductModel cartProduct = cart.getCartProducts().stream()
            .findAny().get();

        when(this.cartService.findById(cartId)).thenReturn(cart);
        when(this.cartService.removeProductFromCart(cartId, accountId, cartProduct.getProductId()))
            .thenReturn(cart);

        this.cartBusinessService.removeProductFromCart(cartId, accountId, cartProduct.getProductId());

        verify(this.cartService).findById(cartId);
        verify(this.productService).revertBackProductQuantity(cartProduct.getProductId(), cartProduct.getSelectedQuantity());
        verify(this.cartService).removeProductFromCart(cartId, accountId, cartProduct.getProductId());
    }

    @Test
    void removeCart() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String cartId = cartModel.getId();

        when(this.cartService.findById(cartId)).thenReturn(cartModel);
        
        this.cartBusinessService.removeCart(cartId);

        verify(this.cartService).findById(cartId);
        verify(this.cartService).removeCart(cartId);
        cartModel.getCartProducts().forEach(cartProduct -> {
            verify(this.productService).revertBackProductQuantity(cartProduct.getProductId(), cartProduct.getSelectedQuantity());
        });
    }

}
