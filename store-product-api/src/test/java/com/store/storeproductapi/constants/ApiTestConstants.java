package com.store.storeproductapi.constants;

public class ApiTestConstants {
    
    // internal endpoints

    public static final String INTERNAL_ACCOUNTS = "/api/internal/accounts";
    public static final String INTERNAL_ACCOUNTS_WITH_ID = INTERNAL_ACCOUNTS + "/{subject_id}";
    public static final String INTERNAL_ACCOUNTS_WITH_ID_INACTIVE = INTERNAL_ACCOUNTS_WITH_ID + "/inactive";

    public static final String INTERNAL_CART = "/api/internal/carts";
    public static final String INTERNAL_CART_WITH_ID = INTERNAL_CART + "/{cart_id}";

    public static final String INTERNAL_CATEGORIES = "/api/internal/categories";
    public static final String INTERNAL_CATEGORIES_WITH_ID = INTERNAL_CATEGORIES + "/{category_id}";
    public static final String INTERNAL_CATEGORIES_WITH_ID_ASSIGN = INTERNAL_CATEGORIES_WITH_ID + "/assign";
    public static final String INTERNAL_CATEGORIES_WITH_ID_PRODUCTS_WITH_PRODUCT_ID = INTERNAL_CATEGORIES_WITH_ID + "/products/{product_id}";
    
    public static final String INTERNAL_PRODUCTS = "/api/internal/products";
    public static final String INTERNAL_PRODUCTS_WITH_ID = INTERNAL_PRODUCTS + "/{product_id}";
    public static final String INTERNAL_PRODUCTS_WITH_ID_REMOVE = INTERNAL_PRODUCTS_WITH_ID + "/remove";

    // public endpoints

    public static final String ACCOUNTS = "/api/accounts";
    public static final String ACCOUNTS_WITH_ID = ACCOUNTS + "/{account_id}";

    public static final String CARTS = "/api/carts";
    public static final String CARTS_WITH_ID = CARTS + "/{cart_id}";
    public static final String CARTS_ADD = CARTS + "/add";
    public static final String CARTS_REMOVE = CARTS + "/remove";
    
}
