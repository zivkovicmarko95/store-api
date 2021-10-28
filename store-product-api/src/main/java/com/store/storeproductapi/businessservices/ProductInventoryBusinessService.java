package com.store.storeproductapi.businessservices;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.services.ProductService;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryBusinessService {
    
    private final ProductService productService;

    @Autowired
    public ProductInventoryBusinessService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Method that verify that product is visible and product quantity 
     * is greater than 0
     * 
     * @param productId ID of the product
     */
    public void verifyProductsQuantity(final String productId) {
        ArgumentVerifier.verifyNotNull(productId);
        
        final ProductModel product = productService.findById(productId);

        if (!product.isVisible()) {
            
            throw new ResourceStateException(
                String.format(
                    "Product with productId %s is not visible",
                    productId
                )
            );
        }

        if (product.getQuantity() == 0) {
            
            throw new ResourceStateException(
                String.format(
                    "Quantity of the product %s is equal to 0.",
                    productId
                )
            );
        }
    }

}
