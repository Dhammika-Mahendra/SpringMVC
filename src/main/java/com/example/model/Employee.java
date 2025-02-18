package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPID")
    private int id;

    @Column(name = "EMPNAME")
    private String name;

    @Column(name = "EMPDOB")
    private String dob;

    @Column(name = "EMPSALARY")
    private int salary;

    @Column(name = "EMPPLACE")
    private String place;

    // Getters and Setters
}
