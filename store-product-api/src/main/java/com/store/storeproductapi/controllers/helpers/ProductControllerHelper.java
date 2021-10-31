package com.store.storeproductapi.controllers.helpers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.businessservices.ProductInventoryBusinessService;
import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.mappers.ProductMapper;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.api.Product;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.transferobjects.DeleteResultTO;
import com.store.storeproductapi.transferobjects.ProductTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductControllerHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductControllerHelper.class);

    private ProductService productService;
    private ProductInventoryBusinessService productInventoryBusinessService;

    @Autowired
    public ProductControllerHelper(ProductService productService, ProductInventoryBusinessService productInventoryBusinessService) {
        this.productService = productService;
        this.productInventoryBusinessService = productInventoryBusinessService;
    }

    // path -> /products?id=value1&title=value2
    public ProductTO productsProductIdAndTitleGet(final String productId, final String productTitle) {

        final ProductModel foundProduct;

        if (productTitle == null) {
            foundProduct = this.productService.findById(productId);
        } else if (productId != null && productTitle != null) {
            foundProduct = this.productService.findById(productId);
            final ProductModel foundProductByTitle = this.productService.findByTitle(productTitle);

            if (!foundProduct.equals(foundProductByTitle)) {
                throw new ResourceStateException(
                    String.format(
                        "Product with provided id %s and title %s do not exist",
                        productId,
                        productTitle
                    )
                );
            }
        } else {
            foundProduct = this.productService.findByTitle(productTitle);
        }

        productInventoryBusinessService.verifyProductsQuantity(foundProduct.getId());

        return ProductMapper.mapRepoToProductTO(foundProduct);
    }

    // path -> /products
    public Set<ProductTO> productsGet(final int page) {

        final List<ProductModel> products = productService.findAll(page)
                .getContent();

        return products.stream()
            .map(product -> ProductMapper.mapRepoToProductTO(product))
            .collect(Collectors.toSet());
    }

    // path -> /internal/products
    public ProductTO internalProductsPost(final Product product) {

        LOGGER.info("Creating product with title {} and description {}", product.getTitle(), product.getDescription());

        final ProductModel createdProduct = this.productService.createProduct(product.getTitle(), product.getPrice(), 
                product.getDescription(), product.getImgUrl(), product.getQuantity());

        LOGGER.info("Product created ... OK");
            
        return ProductMapper.mapRepoToProductTO(createdProduct);
    }

    // path -> /internal/products/{product_id}
    public DeleteResultTO internalProductsProductIdDelete(final String productId) {

        final ProductModel removedProduct = this.productService.removeProduct(productId);

        LOGGER.info("Updating product status -> OK. Product: {}", removedProduct);

        return new DeleteResultTO()
                .resourceId(removedProduct.getId())
                .message("Product has been removed.");
    }

    // path -> /internal/products/{product_id}/remove
    public DeleteResultTO internalProductsProductIdRemoveDelete(final String productId) {

        LOGGER.info("Removing product with product Id {} from the DB", productId);
        
        this.productService.hardRemoveProduct(productId);

        LOGGER.info("Product removed ... OK");

        return new DeleteResultTO()
                .resourceId(productId)
                .message("Product has been removed from the DB.");
    }
}
