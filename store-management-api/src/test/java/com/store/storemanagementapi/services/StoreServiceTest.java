package com.store.storemanagementapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import com.store.storemanagementapi.enums.StoreStatusEnum;
import com.store.storemanagementapi.exceptions.ResourceExistException;
import com.store.storemanagementapi.exceptions.ResourceNotFoundException;
import com.store.storemanagementapi.models.StoreModel;
import com.store.storemanagementapi.repositories.StoreRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class StoreServiceTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private StoreRepository storeRepository;

    private StoreService storeService;

    @BeforeEach
    void setup() {
        storeService = new StoreService(storeRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.storeRepository);
    }

    @Test
    void getById() {

        final StoreModel store = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final String id = store.getId();

        when(this.storeRepository.findById(id)).thenReturn(Optional.of(store));

        final StoreModel result = this.storeService.getById(id);

        verifyStoreModel(result, store);

        verify(this.storeRepository).findById(id);
    }

    @Test
    void getById_notFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.storeService.getById(id))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.storeRepository).findById(id);
    }

    @Test
    void getAll() {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);

        when(this.storeRepository.findAll()).thenReturn(storeModels);

        final List<StoreModel> result = this.storeService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).containsAll(storeModels);

        verify(this.storeRepository).findAll();
    }

    @Test
    void getAllByCity() {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String city = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findAllByCity(city)).thenReturn(storeModels);

        final List<StoreModel> result = this.storeService.getAllByCity(city);

        IntStream.range(0, storeModels.size()).forEach(i -> {
            verifyStoreModel(result.get(i), storeModels.get(i));
        });

        verify(this.storeRepository).findAllByCity(city);
    }

    @Test
    void getAllByZipcode() {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String zipCode = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findAllByZipCode(zipCode)).thenReturn(storeModels);

        final List<StoreModel> result = this.storeService.getAllByZipcode(zipCode);

        IntStream.range(0, storeModels.size()).forEach(i -> {
            verifyStoreModel(result.get(i), storeModels.get(i));
        });

        verify(this.storeRepository).findAllByZipCode(zipCode);
    }

    @Test
    void getAllOpenedByCity() {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String city = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findAllByStoreStatusAndCity(StoreStatusEnum.OPEN, city)).thenReturn(storeModels);

        final List<StoreModel> result = this.storeService.getAllOpenedByCity(city);

        IntStream.range(0, storeModels.size()).forEach(i -> {
            verifyStoreModel(result.get(i), storeModels.get(i));
        });

        verify(this.storeRepository).findAllByStoreStatusAndCity(StoreStatusEnum.OPEN, city);
    }

    @Test
    void getAllOpenedByZipcode() {

        final List<StoreModel> storeModels = PODAM_FACTORY.manufacturePojo(List.class, StoreModel.class);
        final String zipCode = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findAllByStoreStatusAndZipCode(StoreStatusEnum.OPEN, zipCode)).thenReturn(storeModels);

        final List<StoreModel> result = this.storeService.getAllOpenedByZipcode(zipCode);

        IntStream.range(0, storeModels.size()).forEach(i -> {
            verifyStoreModel(result.get(i), storeModels.get(i));
        });

        verify(this.storeRepository).findAllByStoreStatusAndZipCode(StoreStatusEnum.OPEN, zipCode);
    }

    @Test
    void createStore() {

        final String city = PODAM_FACTORY.manufacturePojo(String.class);
        final String street = PODAM_FACTORY.manufacturePojo(String.class);
        final String streetNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final String phoneNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final String zipCode = PODAM_FACTORY.manufacturePojo(String.class);
        final StoreModel store = new StoreModel(city, street, streetNumber, phoneNumber, zipCode, StoreStatusEnum.TO_BE_OPENED, Set.of());

        when(this.storeRepository.save(store)).thenReturn(store);

        final StoreModel result = this.storeService.createStore(city, street, streetNumber, phoneNumber, zipCode);

        verifyStoreModel(result, store);

        verify(this.storeRepository).save(store);
    }

    @ParameterizedTest
    @EnumSource(value = StoreStatusEnum.class)
    void updateStore(final StoreStatusEnum status) {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String city = PODAM_FACTORY.manufacturePojo(String.class);
        final String street = PODAM_FACTORY.manufacturePojo(String.class);
        final String streetNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final String phoneNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final String zipCode = PODAM_FACTORY.manufacturePojo(String.class);
        final StoreModel store = PODAM_FACTORY.manufacturePojo(StoreModel.class);

        when(this.storeRepository.findById(id)).thenReturn(Optional.of(store));
        when(this.storeRepository.save(store)).thenReturn(store);

        final StoreModel result = this.storeService.updateStore(id, city, street, streetNumber, phoneNumber, zipCode, status);

        verifyStoreModel(result, store);

        verify(this.storeRepository).findById(id);
        verify(this.storeRepository).save(store);
    }

    @ParameterizedTest
    @EnumSource(value = StoreStatusEnum.class)
    void updateStore_storeNotFound(final StoreStatusEnum status) {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String city = PODAM_FACTORY.manufacturePojo(String.class);
        final String street = PODAM_FACTORY.manufacturePojo(String.class);
        final String streetNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final String phoneNumber = PODAM_FACTORY.manufacturePojo(String.class);
        final String zipCode = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.storeService.updateStore(id, city, street, streetNumber, phoneNumber, zipCode, status))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.storeRepository).findById(id);
    }

    @Test
    void addEmployeeToStore() {

        final StoreModel store = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final String id = store.getId();
        final String employeeId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findById(id)).thenReturn(Optional.of(store));
        when(this.storeRepository.save(store)).thenReturn(store);

        final StoreModel result = this.storeService.addEmployeeToStore(id, employeeId);

        verifyStoreModel(result, store);

        verify(this.storeRepository).findById(id);
        verify(this.storeRepository).save(store);
    }

    @Test
    void addEmployeeToStore_storeNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String employeeId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.storeService.addEmployeeToStore(id, employeeId))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.storeRepository).findById(id);
    }

    @Test
    void addEmployeeToStore_employeeExist() {

        final StoreModel store = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final String id = store.getId();
        final String employeeId = store.getEmployeeIds().stream()
                .findAny().get();

        when(this.storeRepository.findById(id)).thenReturn(Optional.of(store));

        assertThatThrownBy(() -> this.storeService.addEmployeeToStore(id, employeeId))
                .isExactlyInstanceOf(ResourceExistException.class)
                .hasNoCause();

        verify(this.storeRepository).findById(id);
    }

    @Test
    void removeEmployeeFromStore() {

        final StoreModel store = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final String id = store.getId();
        final String employeeId = store.getEmployeeIds().stream()
                .findAny().get();

        when(this.storeRepository.findById(id)).thenReturn(Optional.of(store));
        when(this.storeRepository.save(store)).thenReturn(store);

        final StoreModel result = this.storeService.removeEmployeeFromStore(id, employeeId);

        verifyStoreModel(result, store);

        verify(this.storeRepository).findById(id);
        verify(this.storeRepository).save(store);
    }

    @Test
    void removeEmployeeFromStore_storeNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String employeeId = PODAM_FACTORY.manufacturePojo(String.class);
        
        when(this.storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.storeService.removeEmployeeFromStore(id, employeeId))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.storeRepository).findById(id);
    }

    @Test
    void removeEmployeeFromStore_employeeNotExist() {

        final StoreModel store = PODAM_FACTORY.manufacturePojo(StoreModel.class);
        final String id = store.getId();
        final String employeeId = PODAM_FACTORY.manufacturePojo(String.class);
        
        when(this.storeRepository.findById(id)).thenReturn(Optional.of(store));

        assertThatThrownBy(() -> this.storeService.removeEmployeeFromStore(id, employeeId))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasNoCause();

        verify(this.storeRepository).findById(id);
    }

    @Test
    void removeById() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        this.storeService.removeById(id);

        verify(this.storeRepository).deleteById(id);
    }

    private void verifyStoreModel(StoreModel result, StoreModel store) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(store.getId());
        assertThat(result.getCity()).isEqualTo(store.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(store.getPhoneNumber());
        assertThat(result.getStoreStatus()).isEqualTo(store.getStoreStatus());
        assertThat(result.getStreet()).isEqualTo(store.getStreet());
        assertThat(result.getStreetNumber()).isEqualTo(store.getStreetNumber());
        assertThat(result.getZipCode()).isEqualTo(store.getZipCode());
        assertThat(result.getEmployeeIds()).containsAll(store.getEmployeeIds());

    }

}
