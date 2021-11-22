package com.store.storemanagementapi.services.impl;

import com.store.storemanagementapi.repositories.StoreRepository;
import com.store.storemanagementapi.services.StoreService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

}
