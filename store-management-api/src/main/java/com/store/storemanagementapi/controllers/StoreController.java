package com.store.storemanagementapi.controllers;

import java.util.List;

import com.store.storemanagementapi.mappers.StoreMapper;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.services.StoreService;
import com.store.storemanagementapi.transferobjects.StoreTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    
    private final StoreService storeService;

    @Autowired
    public StoreController(final StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreTO> storesStoreIdGet(@PathVariable final String storeId) {

        final StoreModel storeModel = this.storeService.getById(storeId);

        return new ResponseEntity<>(
                StoreMapper.mapRepoToStoreTO(storeModel),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<StoreTO>> storesStoreGet(@RequestParam(required = false) final String city, 
            @RequestParam(required = false) final boolean isOpen) {

        final List<StoreModel> stores;

        if (city != null && isOpen) {
            stores = this.storeService.getAllOpenedByCity(city);
        } else if (city != null) {
            stores = this.storeService.getAllByCity(city);
        } else {
            stores = this.storeService.getAll();   
        }

        return new ResponseEntity<>(
                StoreMapper.mapRepoStoreTOs(stores),
                HttpStatus.OK
        );
    }

    @GetMapping("/zipcode/{zipCode}")
    public ResponseEntity<List<StoreTO>> storesStoreZipCodeZipCodeGet(@PathVariable final String zipCode) {

        return new ResponseEntity<>(
                StoreMapper.mapRepoStoreTOs(this.storeService.getAllByZipcode(zipCode)),
                HttpStatus.OK
        );
    }

}
