package com.store.storeproductapi.businessservices;

import java.util.Set;

import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.models.CartProductModel;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.services.CartService;
import com.store.storeproductapi.services.ProductService;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class CartBusinessService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CartBusinessService.class);
    
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartBusinessService(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Method that creates cart and updates it by adding product to the cart
     * 
     * @param productId ID of the product
     * @param accountId ID of the account
     * @param quantity quantity of the product
     * @return Created {@link CartModel}
     */
    public CartModel createCartAndAddProduct(final String productId, final String accountId, final int quantity) {
        ArgumentVerifier.verifyNotNull(productId, accountId);

        productService.updateProductQuantity(productId, quantity);
        
        return cartService.createCart(productId, accountId, quantity);
    }

    /**
     * Method that adds product to cart
     * 
     * @param cartId ID of the cart
     * @param productId ID of the product
     * @param accountId ID of the account
     * @param quantity Quantity of the product
     * @return Tuple of updated {@link CartModel} and updated {@link ProductModel}
     */
    public Tuple2<CartModel, ProductModel> addProductToCart(final String cartId, final String productId, final String accountId, final int quantity) {
        ArgumentVerifier.verifyNotNull(cartId, productId, accountId);

        final CartModel cart = cartService.addToCart(cartId, accountId, productId, quantity);
        final ProductModel product = productService.updateProductQuantity(productId, quantity);

        return Tuples.of(cart, product);
    }

    /**
     * Method that removes product from cart. The method will remove cart if it is empty
     * 
     * @param cartId ID of the cart
     * @param accountId ID of the account
     * @param productId ID of the product
     */
    public void removeProductFromCart(final String cartId, final String accountId, final String productId) {
        ArgumentVerifier.verifyNotNull(cartId, accountId, productId);

        final CartModel cart = cartService.findById(cartId);
        final Set<CartProductModel> cartProducts = cart.getCartProducts();

        final CartProductModel cartProduct = cartProducts.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new StoreResourceNotFoundException(
                    String.format(
                        "Product with id %s is not found in the cart with id %s",
                        productId,
                        cartId
                    )
                ));
                
        this.productService.revertBackProductQuantity(productId, cartProduct.getSelectedQuantity());

        final CartModel updatedCart = cartService.removeProductFromCart(cartId, accountId, productId);

        if (CollectionUtils.isEmpty(updatedCart.getCartProducts())) {
            
            LOGGER.info("Removing cart because it does not have any assigned product. Cart id: {}", cartId);
            
            cartService.removeCart(cartId);
        }
    }

    /**
     * Method used for removing cart and reverting back product quantities
     * 
     * @param cartId ID of the cart
     */
    public void removeCart(final String cartId) {
        ArgumentVerifier.verifyNotNull(cartId);

        final CartModel cartModel = this.cartService.findById(cartId);
        final Set<CartProductModel> cartProductModels = cartModel.getCartProducts();

        this.cartService.removeCart(cartId);

        cartProductModels.forEach(cartProduct -> {
            this.productService.revertBackProductQuantity(cartProduct.getProductId(), cartProduct.getSelectedQuantity());
        });
        
        LOGGER.info("Removed cart with cartId: {}", cartId);
    }

}
