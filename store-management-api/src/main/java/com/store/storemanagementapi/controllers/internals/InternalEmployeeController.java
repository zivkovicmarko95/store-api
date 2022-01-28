package com.store.storemanagementapi.controllers.internals;

import java.util.List;

import com.store.storemanagementapi.enums.EmployeeStatusEnum;
import com.store.storemanagementapi.mappers.EmployeeMapper;
import com.store.storemanagementapi.models.EmployeeModel;
import com.store.storemanagementapi.models.api.EmployeeCreate;
import com.store.storemanagementapi.models.api.EmployeeUpdate;
import com.store.storemanagementapi.services.EmployeeService;
import com.store.storemanagementapi.transferobjects.EmployeeTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/employees")
public class InternalEmployeeController {
    
    private final EmployeeService employeeService;

    @Autowired
    public InternalEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeTO> employeesEmployeeIdGet(@PathVariable final String employeeId) {

        final EmployeeModel employeeModel = this.employeeService.getById(employeeId);

        return new ResponseEntity<>(
                EmployeeMapper.mapRepoToEmployeeTO(employeeModel), 
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<EmployeeTO> employeesGetByParamFirstnameAndParamLastname(@RequestParam final String firstname, @RequestParam final String lastname) {
        
        final EmployeeModel employeeModel = this.employeeService.getByFirstnameAndLastName(firstname, lastname);
        
        return new ResponseEntity<>(
                EmployeeMapper.mapRepoToEmployeeTO(employeeModel),
                HttpStatus.OK
        );
    }

    @GetMapping("/status/{employeeStatus}")
    public ResponseEntity<List<EmployeeTO>> employeesStatusEmployeeStatusGet(@PathVariable final String employeeStatus) {

        final List<EmployeeModel> employeeModels = this.employeeService.getAllByStatus(EmployeeStatusEnum.resolveEmployeeStatus(employeeStatus));

        return new ResponseEntity<>(
                EmployeeMapper.mapReposToEmployeeTOs(employeeModels),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<EmployeeTO> employeesPost(@RequestBody final EmployeeCreate employeeCreate) {

        final EmployeeModel employeeModel = this.employeeService.createEmployee(employeeCreate.getFirstname(), employeeCreate.getLastname(), employeeCreate.getAddress(), 
                employeeCreate.getPhoneNumber(), employeeCreate.getSalary());
        
        return new ResponseEntity<>(
                EmployeeMapper.mapRepoToEmployeeTO(employeeModel), 
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{employeeId}/inactive")
    public ResponseEntity<EmployeeTO> employeesEmployeeIdInactivePost(@PathVariable final String employeeId) {

        final EmployeeModel employeeModel = this.employeeService.updateEmployeeStatusToInactive(employeeId);
        
        return new ResponseEntity<>(
                EmployeeMapper.mapRepoToEmployeeTO(employeeModel),
                HttpStatus.OK
        );
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeTO> employeesEmployeeIdPut(@RequestBody final EmployeeUpdate employeeUpdate) {

        final EmployeeModel employeeModel = this.employeeService.updateEmployee(employeeUpdate.getId(), employeeUpdate.getFirstname(), employeeUpdate.getLastname(), 
                employeeUpdate.getAddress(), employeeUpdate.getPhonenumber(), employeeUpdate.getSalary());

        return new ResponseEntity<>(
                EmployeeMapper.mapRepoToEmployeeTO(employeeModel),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> employeesEmployeeIdDelete(@PathVariable final String employeeId) {
        this.employeeService.removeById(employeeId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
