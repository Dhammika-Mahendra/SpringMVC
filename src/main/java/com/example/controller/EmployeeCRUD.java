package com.example.controller;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/CRUD/emp")
public class EmployeeCRUD {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAllEmployees();
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@RequestParam (name="id") Long id) {
        Optional<Employee> employeeData = employeeRepository.findEmployeeById(id);
        return employeeData.map(employee -> new ResponseEntity<>(employee, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create employee
    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
        try {
            boolean created = employeeRepository.createEmployee(employee);
            if (created) {
                return new ResponseEntity<>("Employee was created successfully.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create employee.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update employee
    @PutMapping()
    public ResponseEntity<String> updateEmployee(@RequestParam (name="id") Long id, @RequestBody Employee employee) {
        try {
            Optional<Employee> emp= employeeRepository.findEmployeeById(id);
            if (emp.isEmpty()) {
                return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
            }

            boolean updated = employeeRepository.updateEmployee(employee);
            if (updated) {
                return new ResponseEntity<>("Employee was updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update employee.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete employee
    @DeleteMapping()
    public ResponseEntity<String> deleteEmployee(@RequestParam (name="id") Long id) {
        try {
            boolean deleted = employeeRepository.deleteById(id);
            if (deleted) {
                return new ResponseEntity<>("Employee was deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Find employees by place
    @GetMapping("/place")
    public ResponseEntity<List<Employee>> getEmployeesByPlace(@RequestParam (name="place") String place) {
        List<Employee> employees = employeeRepository.findEmployeeByPlace(place);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

}