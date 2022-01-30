package com.store.storemanagementapi.controllers.internals;

import java.util.List;
import java.util.stream.Collectors;

import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storemanagementapi.mappers.InternalStoreMapper;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.models.api.StoreCreate;
import com.store.storemanagementapi.models.api.StoreUpdate;
import com.store.storemanagementapi.services.EmployeeService;
import com.store.storemanagementapi.services.StoreService;
import com.store.storemanagementapi.transferobjects.InternalStoreTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/stores")
public class InternalStoreController {
    
    private final StoreService storeService;
    private final EmployeeService employeeService;

    @Autowired
    public InternalStoreController(final StoreService storeService, final EmployeeService employeeService) {
        this.storeService = storeService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<InternalStoreTO> internalStoresPost(@RequestBody StoreCreate storeCreate) {

        final String city = storeCreate.getCity();
        final String street = storeCreate.getStreet();
        final String streetNumber = storeCreate.getStreetNumber();
        final String phoneNumber = storeCreate.getPhoneNumber();
        final String zipcode = storeCreate.getZipcode();

        final StoreModel storeModel = this.storeService.createStore(city, street, streetNumber, phoneNumber, zipcode);

        return new ResponseEntity<>(
                InternalStoreMapper.mapRepoToInternalStoreTO(storeModel, List.of()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<InternalStoreTO> internalStoresPut(@PathVariable final String storeId, @RequestBody StoreUpdate storeUpdate) {

        final String city = storeUpdate.getCity();
        final String street = storeUpdate.getStreet();
        final String streetNumber = storeUpdate.getStreetNumber();
        final String phoneNumber = storeUpdate.getPhoneNumber();
        final String zipcode = storeUpdate.getZipcode();
        final StoreStatusEnum status = StoreStatusEnum.resolveStoreStatus(storeUpdate.getStatus());

        final StoreModel storeModel = this.storeService.updateStore(storeId, city, street, streetNumber, phoneNumber, zipcode, status);

        final List<EmployeeModel> employeeModels = storeModel.getEmployeeIds().stream()
                .map(employeeId -> this.employeeService.getById(employeeId))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                InternalStoreMapper.mapRepoToInternalStoreTO(storeModel, employeeModels),
                HttpStatus.ACCEPTED
        );
    }

}
