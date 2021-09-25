package com.store.storeproductapi.services;

import com.store.storeproductapi.models.ProductModel;

import org.springframework.data.domain.Page;

public interface ProductService {

    /**
     * Method that returns {@link ProductModel} by provided product ID
     * 
     * @param id ID of the product
     * @return model of type {@link ProductModel}
     */
    ProductModel findById(String id);

    /**
     * Method that returns {@link ProductModel} by provided product title
     * 
     * @param title Title of the product
     * @return model of type {@link ProductModel}
     */
    ProductModel findByTitle(String title);

    /**
     * Method that returns page of products
     * 
     * @param page page number
     * @return Page of objects of type {@link ProductModel}
     */
    Page<ProductModel> findAll(int page);

    /**
     * Method used for creating new {@link ProductModel}
     * 
     * @param title Title of the product
     * @param price Price of the product
     * @param description Description of the product
     * @param imgUrl URL of the product image
     * @param quantity Quantity of the product
     * @return created {@link ProductModel}
     */
    ProductModel createProduct(String title, float price, String description, String imgUrl, int quantity);

    /**
     * Method that updates quantity of the particular product
     * 
     * @param productId ID of the product
     * @param quantity quantity to be updated
     * @return Updated {@link ProductModel}
     */
    ProductModel updateProductQuantity(final String productId, final int quantity);

    /**
     * Method that updates product quantity by reverting it back
     * 
     * @param productId ID of the product
     * @param quanity quantity of the product
     * @return updated {@link ProductModel}
     */
    ProductModel revertBackProductQuantity(final String productId, final int quanity); 

    /**
     * Method that sets visible status of {@link ProductModel} to be false
     * 
     * @param id ID of the product
     * @return model of type {@link ProductModel}
     */
    ProductModel removeProduct(String id);

    /**
     * Method that removes {@link ProductModel} from the DB
     * 
     * @param id ID of the product
     */
    void hardRemoveProduct(String id);

}
