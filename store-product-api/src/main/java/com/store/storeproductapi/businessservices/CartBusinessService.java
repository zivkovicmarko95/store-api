package com.store.storeproductapi.businessservices;

import com.store.storeproductapi.models.CartModel;
import com.store.storeproductapi.services.CartService;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
     * Method that removes product from cart. The method will remove cart if it is empty
     * 
     * @param cartId ID of the cart
     * @param accountId ID of the account
     * @param productId ID of the product
     */
    public void removeProductFromCart(final String cartId, final String accountId, final String productId) {
        ArgumentVerifier.verifyNotNull(cartId, accountId, productId);

        final CartModel cart = cartService.removeProductFromCart(cartId, accountId, productId);

        if (CollectionUtils.isEmpty(cart.getCartProducts())) {
            
            LOGGER.info("Removing cart because it does not have any assigned product. Cart id: {}", cartId);
            
            cartService.removeCart(cartId);
        }
    }

}
