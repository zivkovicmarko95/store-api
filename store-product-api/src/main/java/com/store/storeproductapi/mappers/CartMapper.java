package com.store.storeproductapi.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.transferobjects.CartProductTO;
import com.store.storeproductapi.transferobjects.CartTO;

public class CartMapper {
    
    private CartMapper() {
        
    }

    public static CartTO mapRepoToCartTO(final CartModel cart, final Set<ProductModel> products) {
        
        return new CartTO()
            .id(cart.getId())
            .cartProducts(mapRepoToCartProducts(products, cart.getCartProducts()));
    }

    private static Set<CartProductTO> mapRepoToCartProducts(final Set<ProductModel> products, final Set<CartProductModel> cartProducts) {

        return products.stream().map(product -> {

            final CartProductModel cartProductModel = cartProducts.stream()
                .filter(cartProduct -> cartProduct.getProductId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new StoreResourceNotFoundException(
                    String.format(
                        "Provided product models and cart product models does not match. ProductModels: %s, CartProductModels: %s",
                        products,
                        cartProducts
                    )
                ));

            return new CartProductTO()
                .id(product.getId())
                .price(product.getPrice())
                .title(product.getTitle())
                .selectedQuantity(cartProductModel.getSelectedQuantity());

        }).collect(Collectors.toSet());
    }
}
