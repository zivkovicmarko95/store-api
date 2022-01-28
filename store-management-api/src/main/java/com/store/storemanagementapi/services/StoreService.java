package com.store.storemanagementapi.services;

import java.util.List;
import java.util.Set;

import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storemanagementapi.exceptions.ResourceExistException;
import com.store.storemanagementapi.exceptions.ResourceNotFoundException;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.repositories.StoreRepository;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(final StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreModel getById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Store with id %s is not found.", id)
                ));
    }

    public List<StoreModel> getAll() {
        return this.storeRepository.findAll();
    }

    public List<StoreModel> getAllByCity(final String city) {
        ArgumentVerifier.verifyNotNull(city);

        return this.storeRepository.findAllByCity(city);
    }

    public List<StoreModel> getAllByZipcode(final String zipcode) {
        ArgumentVerifier.verifyNotNull(zipcode);

        return this.storeRepository.findAllByZipCode(zipcode);
    }

    public List<StoreModel> getAllOpenedByCity(final String city) {
        ArgumentVerifier.verifyNotNull(city);

        return this.storeRepository.findAllByStoreStatusAndCity(StoreStatusEnum.OPEN, city);
    }

    public List<StoreModel> getAllOpenedByZipcode(final String zipcode) {
        ArgumentVerifier.verifyNotNull(zipcode);

        return this.storeRepository.findAllByStoreStatusAndZipCode(StoreStatusEnum.OPEN, zipcode);
    }

    public StoreModel createStore(final String city, final String street, final String streetNumber,
            final String phoneNumber, final String zipCode) {
        ArgumentVerifier.verifyNotNull(city, street, streetNumber, phoneNumber, zipCode);

        final StoreModel store = new StoreModel(city, street, streetNumber, phoneNumber, zipCode, StoreStatusEnum.TO_BE_OPENED, Set.of());
        
        LOGGER.info("Creating store {}.", store);

        return this.storeRepository.save(store);
    }

    public StoreModel updateStore(final String id, final String city, final String street, final String streetNumber, final String phoneNumber,
            final String zipCode, final StoreStatusEnum status) {
        ArgumentVerifier.verifyNotNull(id, city, street, streetNumber, phoneNumber, zipCode, status);

        final StoreModel store = this.getById(id)
                .city(city)
                .street(street)
                .streetNumber(streetNumber)
                .phoneNumber(phoneNumber)
                .zipCode(zipCode)
                .storeStatus(status);

        LOGGER.info("Updating store with id {}.", id);

        return this.storeRepository.save(store);
    }

    public StoreModel addEmployeeToStore(final String storeId, final String employeeId) {
        ArgumentVerifier.verifyNotNull(storeId, employeeId);

        final StoreModel store = this.getById(storeId);
        
        if (!store.getEmployeeIds().add(employeeId)) {
            throw new ResourceExistException(
                String.format(
                    "It's not possible to add employee %s to the store with ID %s.",
                    employeeId, storeId
                )
            );
        }

        LOGGER.info("Adding employee with ID {} to the store with ID {}.", employeeId, storeId);

        return this.storeRepository.save(store);
    }

    public StoreModel removeEmployeeFromStore(final String storeId, final String employeeId) {
        ArgumentVerifier.verifyNotNull(storeId, employeeId);

        final StoreModel store = this.getById(storeId);

        if (!store.getEmployeeIds().remove(employeeId)) {
            throw new ResourceNotFoundException(
                String.format(
                    "It's not possible to remove employee %s from the store %s.",
                    employeeId, storeId
                )
            );
        }

        LOGGER.info("Removing employee with ID {} to the store with ID {}.", employeeId, storeId);

        return this.storeRepository.save(store);
    }

    public void removeById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        LOGGER.info("Removing store with id {}.", id);
        
        this.storeRepository.deleteById(id);
    }

}
