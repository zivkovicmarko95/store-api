package com.store.storeproductapi.services.impl;

import java.util.List;
import java.util.Set;

import com.store.storeproductapi.exceptions.ResourceExistException;
import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.repositories.CategoryRepository;
import com.store.storeproductapi.services.CategoryService;
import com.store.storeproductapi.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryModel findById(String id) {
        ArgumentVerifier.verifyNotNull(id);
        
        return categoryRepository.findById(id)
            .orElseThrow(() -> new StoreResourceNotFoundException(
                String.format(
                    "Category with id %s is not found",
                    id
                )
            ));
    }

    @Override
    public CategoryModel findByTitle(String title) {
        ArgumentVerifier.verifyNotNull(title);
        
        return categoryRepository.findByTitle(title)
            .orElseThrow(() -> new StoreResourceNotFoundException(
                String.format(
                    "Category with provided title %s is not found",
                    title
                )
            ));
    }

    @Override
    public List<CategoryModel> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryModel assignProductToCategory(String productId, String categoryId) {
        ArgumentVerifier.verifyNotNull(productId, categoryId);

        final CategoryModel category = findById(categoryId);

        if (!category.getProductIds().add(productId)) {
            
            throw new ResourceStateException(
                String.format(
                    "Not possible to add productId %s to the category with id %s",
                    productId,
                    categoryId
                )
            );
        }

        LOGGER.info(
            "Assigning product with id {} to the category with category id {}",
            productId,
            categoryId
        );

        return categoryRepository.save(category);
    }

    @Override
    public CategoryModel assignProductsToCategory(Set<String> productIds, String categoryId) {
        ArgumentVerifier.verifyNotNull(categoryId);
        ArgumentVerifier.verifyNotEmpty(productIds);
        
        if (CollectionUtils.isEmpty(productIds)) {
            throw new StoreGeneralException(
                "Provided set of product Ids are empty"
            );
        }

        final CategoryModel category = findById(categoryId);
        
        if (!category.getProductIds().addAll(productIds)) {
            throw new ResourceStateException(
                String.format(
                    "Not possible to add all productIds [%s] to the category with id %s",
                    productIds,
                    categoryId
                )
            );
        }
        
        return categoryRepository.save(category);
    }

    @Override
    public CategoryModel createCategory(String title, String description) {
        ArgumentVerifier.verifyNotNull(title, description);

        final CategoryModel categoryModel = new CategoryModel()
                .title(title);

        final ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withStringMatcher(StringMatcher.EXACT)
                .withIgnoreNullValues();

        final Example<CategoryModel> example = Example.of(categoryModel, exampleMatcher);
        
        if (categoryRepository.findOne(example).isPresent()) {
            throw new ResourceExistException(
                String.format(
                    "Category with provided title exist %s",
                    title
                )
            );
        }

        return categoryRepository.insert(categoryModel.description(description));
    }

    @Override
    public CategoryModel removeCategory(String id) {
        ArgumentVerifier.verifyNotNull(id);

        final CategoryModel category = findById(id);
        category.setVisible(false);
        
        return categoryRepository.save(category);
    }

    @Override
    public void hardRemoveCategory(String id) {
        ArgumentVerifier.verifyNotNull(id);

        final CategoryModel category = findById(id);

        if (category.isVisible()) {
            throw new ResourceStateException(
                String.format(
                    "Removing category with id %s is not possible. Category: %s",
                    id,
                    category
                )
            );
        }
        
        LOGGER.info("Removing category {}", category);
        categoryRepository.delete(category);
    }

}
