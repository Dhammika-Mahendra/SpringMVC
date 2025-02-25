package com.example.controller;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/db")
    public String printEmployeeDetails(){
       List<Employee> employees = employeeRepository.findAll();
         for(Employee employee : employees){
              System.out.println("Emp : "+ employee.getName());
         }
        return "hello";
    }

}