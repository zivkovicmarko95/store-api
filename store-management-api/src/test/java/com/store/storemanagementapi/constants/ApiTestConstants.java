package com.store.storemanagementapi.constants;

public class ApiTestConstants {
    
    public static final String INTERNAL_EMPLOYEES = "/api/internal/employees";
    public static final String INTERNAL_EMPLOYEES_WITH_ID = INTERNAL_EMPLOYEES + "/{employee_id}";
    public static final String INTERNAL_EMPLOYEES_WITH_ID_INACTIVE = INTERNAL_EMPLOYEES_WITH_ID + "/inactive";
    public static final String INTERNAL_EMPLOYEES_WITH_STATUS = INTERNAL_EMPLOYEES + "/status/{employeeStatus}";

    public static final String STORES = "/api/stores";
    public static final String STORES_WITH_ID = STORES + "/{storeId}";
    public static final String STORES_ZIP_CODE = STORES + "/zipcode/{zipcode}";

    public static final String INTERNAL_STORES = "/api/internal/stores";
    public static final String INTERNAL_STORES_WITH_ID = INTERNAL_STORES + "/{storeId}";
    
}
