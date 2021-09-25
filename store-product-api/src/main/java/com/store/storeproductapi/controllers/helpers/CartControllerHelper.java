package com.store.storeproductapi.controllers.helpers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.CartBusinessService;
import com.store.storeproductapi.mappers.CartMapper;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.Cart;
import com.store.storeproductapi.models.api.CartCreate;
import com.store.storeproductapi.services.CartService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.transferobjects.CartTO;
import com.store.storeproductapi.transferobjects.DeleteResultTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.util.function.Tuple2;

@Component
public class CartControllerHelper {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(CartControllerHelper.class);

    @Autowired
    private CartBusinessService cartBusinessService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // path -> /carts/{id}
    public CartTO cartsCartIdGet(final String cartId) {
        
        final CartModel cart = this.cartService.findById(cartId);
        final Set<CartProductModel> cartProducts = cart.getCartProducts();

        final Set<ProductModel> products = cartProducts.stream()
                .map(cartProduct -> this.productService.findById(cartProduct.getProductId()))
                .collect(Collectors.toSet());

        return CartMapper.mapRepoToCartTO(cart, products);
    }

    // path -> /carts
    public CartTO cartsPost(final CartCreate cartCreate) {

        final String productId = cartCreate.getProductId();
        final String accountId = cartCreate.getAccountId();
        final int quantity = cartCreate.getQuantity();

        LOGGER.info("Creating cart - create cart request: {}", cartCreate );

        final CartModel cart = this.cartBusinessService.createCartAndAddProduct(productId, accountId, quantity);
        final ProductModel product = this.productService.findById(productId);

        LOGGER.info("Cart {} created ... OK", cart.getId());

        final Set<ProductModel> products = new HashSet<>();
        products.add(product);

        return CartMapper.mapRepoToCartTO(cart, products);
    }

    // method used for adding products to cart
    // path -> /carts/add
    public CartTO cartsAddPost(final Cart cart) {
        
        final String cartId = cart.getCartId();
        final String productId = cart.getProductId();
        final String accountId = cart.getAccountId();
        final int quantity = cart.getQuantity();

        LOGGER.info("Adding product {} to the cart {}", cart.getProductId(), cart.getCartId());

        final Tuple2<CartModel, ProductModel> cartProduct = this.cartBusinessService.addProductToCart(cartId, productId, accountId, quantity);
        final CartModel cartModel = cartProduct.getT1();

        LOGGER.info("Product {} added to cart {} ... OK", cart.getProductId(), cart.getCartId());

        final Set<CartProductModel> cartProducts = cartModel.getCartProducts();

        final Set<ProductModel> productModels = cartProducts.stream()
                .map(prod -> this.productService.findById(prod.getProductId()))
                .collect(Collectors.toSet());

        return CartMapper.mapRepoToCartTO(cartModel, productModels);
    }

    // path -> /internal/carts
    public DeleteResultTO internalCartsDelete(final String cartId) {

        cartService.removeCart(cartId);

        LOGGER.info("Cart with id {} has been removed", cartId);

        return new DeleteResultTO()
                .resourceId(cartId)
                .message("Cart has been removed.");
    }

}
