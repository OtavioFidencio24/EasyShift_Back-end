package com.MyProject.service;

import com.MyProject.model.Employee;
import com.MyProject.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService  {

    private final  EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee newEmployee){
        return employeeRepository.save(newEmployee);
    }

    public List<Employee> employeesList()
    {
        return employeeRepository.findAll();

    }

    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    public boolean updateEmployee(Employee newEmployee , Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()){
            Employee existingEmployee = employee.get();
            existingEmployee.setName(newEmployee.getName());
            existingEmployee.setFunction(newEmployee.getFunction());
            existingEmployee.setContractType(newEmployee.getContractType());
            existingEmployee.setPhoneNumber(newEmployee.getPhoneNumber());
            employeeRepository.save(existingEmployee);
            return true;
        } else{
            return false;
        }
    }

    public boolean deleteEmployee(Long id)
    {
       if(employeeRepository.findById(id).isPresent()){
           employeeRepository.deleteById(id);
           return true;
       } else {
           return false;
       }

    }
}
