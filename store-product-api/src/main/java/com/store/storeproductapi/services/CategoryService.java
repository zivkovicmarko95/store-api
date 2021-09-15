package com.store.storeproductapi.services;

import java.util.List;
import java.util.Set;

import com.store.storeproductapi.models.CategoryModel;

public interface CategoryService {
    
    /**
     * Method that returns {@link CategoryModel} by provided category ID
     * 
     * @param id ID of the category
     * @return model of type {@link CategoryModel}
     */
    CategoryModel findById(final String id);

    /**
     * Method that returns {@link CategoryModel} by provided category title
     * 
     * @param title Title of the category
     * @return model of type {@link CategoryModel}
     */
    CategoryModel findByTitle(final String title);

    /**
     * Method that returns list of categories
     * 
     * @return List of objects of type {@link CategoryModel}
     */
    List<CategoryModel> findAll();

    /**
     * Method that assigns product to the particular {@link CategoryModel}
     * 
     * @param productId ID of the product
     * @param categoryId ID of the category
     * @return updated {@link CategoryModel}
     */
    CategoryModel assignProductToCategory(final String productId, final String categoryId);

    /**
     * Method that assigns multiple products to the particular {@link CategoryModel}
     * 
     * @param productIds Set of product IDs
     * @param categoryId ID of the category
     * @return updated {@link CategoryModel}
     */
    CategoryModel assignProductsToCategory(final Set<String> productIds, final String categoryId);

    /**
     * Method used for creating new {@link CategoryModel}
     * 
     * @return created {@link CategoryModel}
     */
    CategoryModel createCategory(final String title, final String description);

    /**
     * Method that sets visible status of {@link CategoryModel} to be false
     * 
     * @param id ID of the category
     * @return model of type {@link CategoryModel}
     */
    CategoryModel removeCategory(final String id);

    /**
     * Method that removes {@link CategoryModel} from the DB 
     * 
     * @param id ID of the category
     */
    void hardRemoveCategory(final String id);

}
