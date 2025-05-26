package com.MyProject.controller;

import com.MyProject.model.Employee;
import com.MyProject.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees(){
        return ResponseEntity.ok(employeeService.employeesList());
    }


    @PostMapping
    public ResponseEntity<Employee> createEmployee (@RequestBody Employee employee){
        Employee newEmployee =  employeeService.saveEmployee(employee);
        URI uri =  URI.create("/employees/" + newEmployee.getId());
        return ResponseEntity.created(uri).body(newEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,
                                                   @PathVariable Long id) {
        boolean isUpdated = employeeService.updateEmployee(employee, id);
        if (isUpdated){
            Optional<Employee> updatedEmployee = employeeService.getEmployeeById(id);
            return updatedEmployee.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        boolean isDeleted = employeeService.deleteEmployee(id);
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
