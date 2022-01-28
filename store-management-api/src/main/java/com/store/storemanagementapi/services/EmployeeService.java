package com.store.storemanagementapi.services;

import java.util.Date;
import java.util.List;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storemanagementapi.exceptions.ResourceNotFoundException;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.repositories.EmployeeRepository;
import com.store.storesharedmodule.utils.ArgumentVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeModel getById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        return this.employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Employee with ID %s is not found.", id)
                ));
    }

    public List<EmployeeModel> getAll() {
        return this.employeeRepository.findAll();
    }

    public List<EmployeeModel> getAllByStatus(final EmployeeStatusEnum status) {
        ArgumentVerifier.verifyNotNull(status);

        return this.employeeRepository.findAllByStatus(status);
    }

    public EmployeeModel getByFirstnameAndLastName(final String firstname, final String lastname) {
        ArgumentVerifier.verifyNotNull(firstname, lastname);

        return this.employeeRepository.findByFirstnameAndLastname(firstname, lastname);
    }

    public EmployeeModel createEmployee(final String firstname, final String lastname, final String address, final String phoneNumber,
            final int salary) {
            
        ArgumentVerifier.verifyNotNull(firstname, lastname, address, phoneNumber);
        final EmployeeModel employee = new EmployeeModel(firstname, lastname, address, phoneNumber, salary, EmployeeStatusEnum.ACTIVE);

        LOGGER.info("Creating employee {} {}.", employee.getFirstname(), employee.getLastname());

        return this.employeeRepository.save(employee);
    }

    public EmployeeModel updateEmployeeStatusToInactive(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        final EmployeeModel employeeModel = this.getById(id);
        employeeModel.status(EmployeeStatusEnum.INACTIVE)
                .endOfWorkingDate(new Date());

        LOGGER.info("Employee {} is set to be {}.", employeeModel.getId(), EmployeeStatusEnum.INACTIVE);

        return this.employeeRepository.save(employeeModel);
    }

    public EmployeeModel updateEmployee(final String id, final String firstname, final String lastname, final String address, final String phoneNumber, final int salary) {
        ArgumentVerifier.verifyNotNull(id, firstname, lastname, address, phoneNumber);

        final EmployeeModel employee = this.getById(id)
                .firstname(firstname)
                .lastname(lastname)
                .address(address)
                .phoneNumber(phoneNumber)
                .salary(salary);

        LOGGER.info("Updating employee {}.", id);

        return this.employeeRepository.save(employee);
    }

    public void removeById(final String id) {
        ArgumentVerifier.verifyNotNull(id);

        LOGGER.info("Removing employee with id {}.", id);
        this.employeeRepository.deleteById(id);
    }

}
