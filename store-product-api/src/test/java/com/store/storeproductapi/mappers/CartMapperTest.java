package com.store.storeproductapi.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.transferobjects.CartTO;

import org.junit.jupiter.api.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class CartMapperTest {
 
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void mapRepoToCartTO() {

        final CartModel cart = PODAM_FACTORY.manufacturePojo(CartModel.class);
        final Set<ProductModel> products = generateProducts(cart.getCartProducts());

        final CartTO result = CartMapper.mapRepoToCartTO(cart, products);

        assertThat(result.getId()).isEqualTo(cart.getId());

        result.getCartProducts().forEach(resultCartProduct -> {
            
            final CartProductModel cartProduct = new CartProductModel(resultCartProduct.getId(), resultCartProduct.getSelectedQuantity());

            assertThat(cart.getCartProducts()).contains(cartProduct);
        });
    }

    private Set<ProductModel> generateProducts(final Set<CartProductModel> cartProducts) {
        
        return cartProducts.stream().map(cartProduct -> {
            return PODAM_FACTORY.manufacturePojo(ProductModel.class)
                .id(cartProduct.getProductId());
        }).collect(Collectors.toSet());
    }
}
