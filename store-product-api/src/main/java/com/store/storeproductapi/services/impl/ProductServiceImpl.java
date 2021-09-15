package com.store.storeproductapi.services.impl;

import com.store.storeproductapi.exceptions.ResourceExistException;
import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.repositories.ProductRepository;
import com.store.storeproductapi.services.ProductService;
import com.store.storeproductapi.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final static int SIZE = 48;

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductModel findById(String id) {
        ArgumentVerifier.verifyNotNull(id);
        
        return productRepository.findById(id)
            .orElseThrow(() -> new StoreResourceNotFoundException(
                String.format(
                    "Product with provided id %s is not found",
                    id
                )
            ));
    }

    @Override
    public ProductModel findByTitle(String title) {
        ArgumentVerifier.verifyNotNull(title);

        return productRepository.findByTitle(title)
            .orElseThrow(() -> new StoreResourceNotFoundException(
                String.format(
                    "Product with provided title %s is not found",
                    title
                )
            ));
    }

    @Override
    public ProductModel createProduct(String title, float price, String description, String imgUrl, int quantity) {
        ArgumentVerifier.verifyNotNull(title, description, imgUrl);
        
        LOGGER.info(
            "Creating product with title {}, price {}, description {}, imgUrl {} and quantity {}",
            title,
            price,
            description,
            imgUrl,
            quantity
        );

        final ProductModel product = new ProductModel()
            .title(title);

        final ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withStringMatcher(StringMatcher.EXACT)
                .withIgnoreNullValues();

        final Example<ProductModel> example = Example.of(product, exampleMatcher);

        if (productRepository.findOne(example).isPresent()) {
            throw new ResourceExistException(
                String.format(
                    "Product with provided title %s exists",
                    title
                )
            );
        }

        return productRepository.insert(
            product.price(price)
                    .description(description)
                    .imgUrl(imgUrl)
                    .quantity(quantity)
        );
    }

    @Override
    public ProductModel removeProduct(String id) {
        ArgumentVerifier.verifyNotNull(id);
        
        final ProductModel product = findById(id);
        product.visible(false);

        return productRepository.save(product);
    }

    @Override
    public void hardRemoveProduct(String id) {
        ArgumentVerifier.verifyNotNull(id);
        
        final ProductModel product = findById(id);

        if (product.isVisible()) {
            throw new ResourceStateException(
                String.format(
                    "Removing product with id %s is not possible. Product: %s",
                    id,
                    product
                )
            );
        }

        LOGGER.info("Removing product {}", product);
        productRepository.delete(product);
    }

    @Override
    public Page<ProductModel> findAll(int page) {
        final PageRequest pageRequest = PageRequest.of(page, SIZE);

        return productRepository.findAll(pageRequest);
    }

}
