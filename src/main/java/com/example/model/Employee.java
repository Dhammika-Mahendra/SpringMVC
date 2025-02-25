package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "EMPLOYEES" , schema = "DHAMMIKA")
public class Employee {

    @Id
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

}

