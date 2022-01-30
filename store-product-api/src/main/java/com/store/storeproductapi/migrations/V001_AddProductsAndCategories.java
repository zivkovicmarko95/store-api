package com.store.storeproductapi.migrations;

import java.util.ArrayList;
import java.util.List;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChangeLog(order = "001")
public class V001_AddProductsAndCategories {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(V001_AddProductsAndCategories.class);

    private final static String PRODUCTS_COLLECTION = "products";
    private final static String CATEGORIES_COLLECTION = "categories";

    @ChangeSet(author = "markoz", id = "v001_001__add_products", order = "001")
    public void addProducts(MongockTemplate mongockTemplate) {

        if (mongockTemplate.getCollection(PRODUCTS_COLLECTION).countDocuments() > 0) {
            throw new IllegalArgumentException(
                String.format(
                    "%s already exists and contains documents.", PRODUCTS_COLLECTION
                )
            );
        }

        LOGGER.info("Adding products to the database");

        mongockTemplate.insert(generateProductDocument("Red apple", 100, "Apple is fruit. The organic red apple.", 1000), PRODUCTS_COLLECTION);
        mongockTemplate.insert(generateProductDocument("Green apple", 100, "Apple is fruit. The organic green apple.", 1000), PRODUCTS_COLLECTION);
        mongockTemplate.insert(generateProductDocument("Watermelon", 20, "Watermelon is fruit. The organic watermelon.", 500), PRODUCTS_COLLECTION);

        mongockTemplate.insert(generateProductDocument("Tomato", 50, "Tomato is vegetable. The organic tomato.", 500), PRODUCTS_COLLECTION);
        mongockTemplate.insert(generateProductDocument("Potato", 35, "Potato is vegetable. The organic potato.", 500), PRODUCTS_COLLECTION);

        LOGGER.info("Products are added to the database ... OK");
    }

    @ChangeSet(author = "markoz", id = "v001_002__add_categories", order = "002")
    public void addCategories(MongockTemplate mongockTemplate) {

        if (mongockTemplate.getCollection(CATEGORIES_COLLECTION).countDocuments() > 0) {
            throw new IllegalArgumentException(
                String.format(
                    "%s already exists and contains documents.", PRODUCTS_COLLECTION
                )
            );
        }

        LOGGER.info("Adding categories to the database");

        final List<Document> products = mongockTemplate.findAll(Document.class, PRODUCTS_COLLECTION);
        final List<String> fruitIds = new ArrayList<>();
        final List<String> vegetableIds = new ArrayList<>();

        products.forEach(product -> {
            
            final String id = product.get("_id").toString();

            if (product.get("description").toString().contains("fruit")) {
                fruitIds.add(id);
            } else {
                vegetableIds.add(id);
            }
        });

        mongockTemplate.insert(generateCategoryDocument("Fruits", "Organic fruits", fruitIds), CATEGORIES_COLLECTION);
        mongockTemplate.insert(generateCategoryDocument("Vegetables", "Organic vegetables", vegetableIds), CATEGORIES_COLLECTION);

        LOGGER.info("Categories are added to the database ... OK");
    }

    private Document generateProductDocument(final String title, final int price, final String description,
            final int quantity) {

        Document product = new Document();

        product.append("title", title);
        product.append("price", price);
        product.append("description", description);
        product.append("quantity", quantity);
        product.append("avgUserRating", 0);
        product.append("numberOfVotes", 0);
        product.append("visible", true);

        return product;
    }

    private Document generateCategoryDocument(final String title, final String description, final List<String> productIds) {

        Document category = new Document();

        category.append("title", title);
        category.append("description", description);
        category.append("productIds", productIds);
        category.append("visible", true);

        return category;
    }

}
