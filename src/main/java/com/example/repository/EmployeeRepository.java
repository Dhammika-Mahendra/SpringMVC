package com.example.repository;

import com.example.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Find single employee by ID
    public Employee findById(Long empId) {
        System.out.println(empId);
        return entityManager.find(Employee.class, empId);
    }

    // Get all employees
    public List<Employee> findAll() {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e", Employee.class);
        return query.getResultList();
    }


    // Find employees by place
    public List<Employee> findByPlace(String place) {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.place = :place", Employee.class);
        query.setParameter("place", place);
        return query.getResultList();
    }
}