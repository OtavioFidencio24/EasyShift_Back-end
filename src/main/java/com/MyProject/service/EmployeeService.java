package com.MyProject.service;

import com.MyProject.model.Employee;
import com.MyProject.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling business logic related to employees.
 */
@Service
public class EmployeeService  {

    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for dependency injection.
     * @param employeeRepository the repository used for accessing employee data
     */
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Saves a new employee to the database.
     * @param newEmployee the employee to be saved
     * @return the saved employee entity
     */
    public Employee saveEmployee(Employee newEmployee){
        return employeeRepository.save(newEmployee);
    }

    /**
     * Retrieves the list of all employees.
     * @return a list of employee entities
     */
    public List<Employee> employeesList() {
        return employeeRepository.findAll();
    }

    /**
     * Finds an employee by their ID.
     * @param id the ID of the employee
     * @return an Optional containing the employee if found, or empty if not
     */
    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    /**
     * Updates an existing employee's information.
     * @param newEmployee the updated employee data
     * @param id the ID of the employee to be updated
     * @return true if the update was successful, false if the employee was not found
     */
    public boolean updateEmployee(Employee newEmployee, Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()){
            Employee existingEmployee = employee.get();
            // Update relevant fields
            existingEmployee.setName(newEmployee.getName());
            existingEmployee.setFunction(newEmployee.getFunction());
            existingEmployee.setContractType(newEmployee.getContractType());
            existingEmployee.setPhoneNumber(newEmployee.getPhoneNumber());
            employeeRepository.save(existingEmployee);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an employee by their ID.
     * @param id the ID of the employee to delete
     * @return true if the deletion was successful, false if the employee was not found
     */
    public boolean deleteEmployee(Long id) {
        if(employeeRepository.findById(id).isPresent()){
            employeeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
