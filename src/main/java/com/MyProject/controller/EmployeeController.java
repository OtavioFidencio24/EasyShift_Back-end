package com.MyProject.controller;

import com.MyProject.model.Employee;
import com.MyProject.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Employee entities.
 * Provides endpoints for CRUD operations on employees.
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Returns a list of all registered employees.
     *
     * @return List of Employee objects wrapped in a ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok(employeeService.employeesList());
    }

    /**
     * Creates a new employee record.
     *
     * @param employee the Employee object to be created
     * @return the created Employee object and location URI
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeService.saveEmployee(employee);
        URI uri = URI.create("/employees/" + newEmployee.getId()); // Location of the new resource
        return ResponseEntity.created(uri).body(newEmployee);
    }

    /**
     * Updates an existing employee by ID.
     *
     * @param employee the updated employee data
     * @param id the ID of the employee to update
     * @return the updated employee or NOT_FOUND if the employee does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,
                                                   @PathVariable Long id) {
        boolean isUpdated = employeeService.updateEmployee(employee, id);
        if (isUpdated) {
            Optional<Employee> updatedEmployee = employeeService.getEmployeeById(id);
            return updatedEmployee.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes an employee by ID.
     *
     * @param id the ID of the employee to be deleted
     * @return 204 No Content if deleted, or 404 Not Found if the employee does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean isDeleted = employeeService.deleteEmployee(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Not found
        }
    }
}
