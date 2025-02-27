package com.example.repository;

import com.example.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Employee> findEmployeeById(Long empId) {
        try {
            logger.info("Finding employee with ID: {}", empId);
            Employee employee = entityManager.find(Employee.class, empId);
            return Optional.ofNullable(employee);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID provided for employee lookup: {}", empId, e);
            return Optional.empty();
        } catch (DataAccessException e) {
            logger.error("Database error while finding employee with ID: {}", empId, e);
            return Optional.empty();
        }
    }

    public List<Employee> findAllEmployees() {
        try {
            logger.info("Retrieving all employees");
            TypedQuery<Employee> query = entityManager.createQuery(
                    "SELECT e FROM Employee e", Employee.class);
            return query.getResultList();
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving all employees", e);
            return Collections.emptyList();
        }
    }

    public List<Employee> findEmployeeByPlace(String place) {
        try {
            logger.info("Finding employees from place: {}", place);
            TypedQuery<Employee> query = entityManager.createQuery(
                    "SELECT e FROM Employee e WHERE e.place = :place", Employee.class);
            query.setParameter("place", place);
            return query.getResultList();
        } catch (DataAccessException e) {
            logger.error("Database error while finding employees from place: {}", place, e);
            return Collections.emptyList();
        }
    }


    @Transactional
    public boolean createEmployee(Employee employee) {
        try {
            if (employee.getId() == null) {
                logger.error("Cannot create employee with null ID");
                return false;
            }

            logger.info("Creating new employee with ID: {}", employee.getId());
            entityManager.persist(employee);
            entityManager.flush();
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid employee data: {}", employee, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Database error while creating employee: {}", employee, e);
            return false;
        }
    }

    @Transactional
    public boolean updateEmployee(Employee employee) {
        try {
            if (employee.getId()== null) {
                logger.error("Cannot update employee with null ID");
                return false;
            }

            logger.info("Updating employee with ID: {}", employee.getId());
            Employee managedEmployee = entityManager.find(Employee.class, employee.getId());

            if (managedEmployee == null) {
                logger.error("Employee with ID {} not found for update", employee.getId());
                return false;
            }

            // Update fields
            managedEmployee.setName(employee.getName());
            managedEmployee.setDob(employee.getDob());
            managedEmployee.setSalary(employee.getSalary());
            managedEmployee.setPlace(employee.getPlace());

            entityManager.merge(managedEmployee);
            entityManager.flush();
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid employee data for update: {}", employee, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Database error while updating employee: {}", employee, e);
            return false;
        }
    }

    @Transactional
    public boolean deleteById(Long empId) {
        try {
            logger.info("Deleting employee with ID: {}", empId);
            Employee employee = entityManager.find(Employee.class, empId);

            if (employee == null) {
                logger.error("Employee with ID {} not found for deletion", empId);
                return false;
            }

            entityManager.remove(employee);
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID provided for employee deletion: {}", empId, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Database error while deleting employee with ID: {}", empId, e);
            return false;
        }
    }


}