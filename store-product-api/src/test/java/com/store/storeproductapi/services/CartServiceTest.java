package com.store.storeproductapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.repositories.CartRepository;
import com.store.storeproductapi.services.impl.CartServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class CartServiceTest {
    
    @MockBean
    private CartRepository cartRepository;
    private CartService cartService;

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        cartService = new CartServiceImpl(cartRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    void findById() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String id = cartModel.getId();

        when(this.cartRepository.findById(id)).thenReturn(Optional.of(cartModel));

        final CartModel result = this.cartService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(cartModel);
        assertThat(result.getId()).isEqualTo(cartModel.getId());

        verify(this.cartRepository).findById(id);
    }

    @Test
    void findById_cartNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.cartRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.cartService.findById(id))
            .isExactlyInstanceOf(StoreGeneralException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided id for the cart %s is not valid. Cart is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(id);
    }

    @Test
    void findByAccountId() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String accountId = cartModel.getAccountId();

        when(this.cartRepository.findByAccountId(accountId)).thenReturn(Optional.of(cartModel));

        final CartModel result = this.cartService.findByAccountId(accountId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(cartModel);
        assertThat(result.getAccountId()).isEqualTo(cartModel.getAccountId());

        verify(this.cartRepository).findByAccountId(accountId);
    }

    @Test
    void findByAccountId_cartNotFound() {

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.cartRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.cartService.findByAccountId(accountId))
            .isExactlyInstanceOf(StoreGeneralException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided account does not have assigned any cart id %s",
                    accountId
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findByAccountId(accountId);
    }

    @Test
    void createCart() {

        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);
        final CartModel cartModel = new CartModel(accountId, Set.of(new CartProductModel(productId, quantity)));

        when(this.cartRepository.insert(cartModel)).thenReturn(cartModel);

        final CartModel result = this.cartService.createCart(productId, accountId, quantity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cartModel.getId());
        assertThat(result.getAccountId()).isEqualTo(cartModel.getAccountId());
        assertThat(result.getCartProducts()).isEqualTo(Set.of(new CartProductModel(productId, quantity)));

        verify(this.cartRepository).insert(cartModel);
    }

    @Test
    void addToCart() {

        final CartModel cart = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);
        
        final String cartId = cart.getId();
        final String accountId = cart.getAccountId();

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(this.cartRepository.save(cart)).thenReturn(cart);

        final CartModel result = this.cartService.addToCart(cartId, accountId, productId, quantity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cart.getId());
        assertThat(result.getAccountId()).isEqualTo(cart.getAccountId());
        assertThat(result.getCartProducts()).isEqualTo(cart.getCartProducts());

        verify(this.cartRepository).findById(cartId);
        verify(this.cartRepository).save(cart);
    }

    @Test
    void addToCart_cartNotFound() {

        final String cartId = PODAM_FACTORY.manufacturePojo(String.class);
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.cartService.addToCart(cartId, accountId, productId, quantity))
            .isExactlyInstanceOf(StoreGeneralException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided id for the cart %s is not valid. Cart is not found",
                    cartId
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(cartId);
    }

    @Test
    void addToCart_accountNotMatch() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final int quantity = PODAM_FACTORY.manufacturePojo(Integer.class);
        final String cartId = cartModel.getId();

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.of(cartModel));

        assertThatThrownBy(() -> this.cartService.addToCart(cartId, accountId, productId, quantity))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided account %s does not owns the cart %s",
                    accountId,
                    cartId
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(cartId);
    }

    @Test
    void addToCart_productExistsInCart() {

        final CartModel cart = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final CartProductModel cartProductModel = cart.getCartProducts().stream()
            .findAny().get();
        
        final String cartId = cart.getId();
        final String accountId = cart.getAccountId();

        final int quantity = cartProductModel.getSelectedQuantity();
        final String productId = cartProductModel.getProductId();

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> this.cartService.addToCart(cartId, accountId, productId, quantity))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "It is not possible to add product %s to the cart with quantity of %d",
                    productId,
                    quantity
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(cartId);
    }

    @Test
    void removeProductFromCart() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String cartId = cartModel.getId(); 
        final String accountId = cartModel.getAccountId(); 
        final String productId = cartModel.getCartProducts().stream()
            .findAny()
            .get()
            .getProductId();

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.of(cartModel));
        when(this.cartRepository.save(cartModel)).thenReturn(cartModel);

        final CartModel result = this.cartService.removeProductFromCart(cartId, accountId, productId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(cartModel);

        verify(this.cartRepository).findById(cartId);
        verify(this.cartRepository).save(cartModel);
    }

    @Test
    void removeProductFromCart_cartNotFound() {

        final String cartId = PODAM_FACTORY.manufacturePojo(String.class);
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.cartService.removeProductFromCart(cartId, accountId, productId))
            .isExactlyInstanceOf(StoreGeneralException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided id for the cart %s is not valid. Cart is not found",
                    cartId
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(cartId);
    }

    @Test
    void removeProductFromCart_accountNotMatch() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String accountId = PODAM_FACTORY.manufacturePojo(String.class); 
        
        final String cartId = cartModel.getId(); 
        final String productId = cartModel.getCartProducts().stream()
            .findAny()
            .get()
            .getProductId();

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.of(cartModel));

        assertThatThrownBy(() -> this.cartService.removeProductFromCart(cartId, accountId, productId))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided account %s does not owns the cart %s",
                    accountId,
                    cartId
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(cartId);
    }

    @Test
    void removeProductFromCart_productIdNotMatch() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class); 
        
        final String cartId = cartModel.getId(); 
        final String accountId = cartModel.getAccountId();

        when(this.cartRepository.findById(cartId)).thenReturn(Optional.of(cartModel));

        assertThatThrownBy(() -> this.cartService.removeProductFromCart(cartId, accountId, productId))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided product ID and IDs in the cart does not match",
                    productId,
                    cartModel
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(cartId);
    }

    @Test
    void removeCart() {

        final CartModel cartModel = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final String id = cartModel.getId();

        when(this.cartRepository.findById(id)).thenReturn(Optional.of(cartModel));
        
        this.cartService.removeCart(id);

        verify(this.cartRepository).findById(id);
        verify(this.cartRepository).delete(cartModel);
    }

    @Test
    void removeCart_cartNotFound() {
        
        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.cartRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeCart(id))
            .isExactlyInstanceOf(StoreGeneralException.class)
            .hasMessageStartingWith(
                String.format(
                    "Provided id for the cart %s is not valid. Cart is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.cartRepository).findById(id);
    }

}
