package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SAMPLE", schema = "DHAMMIKA")
public class Sample implements BaseTableModel {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sample_seq")
    @SequenceGenerator(name = "sample_seq", sequenceName = "SAMPLE_SEQ", allocationSize = 1)
    @Column(name = "SAMPLE_ID", nullable = false)
    private Long sampleId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "AGE", nullable = false)
    private int age;

    @Column(name = "PLACE", nullable = false)
    private String place;

    @Column(name = "NIC", nullable = false)
    private String nic;

    @Column(name = "GENDER", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;


    public enum Gender {
        f,m
    }

    // Default constructor required by JPA
    public Sample() {
    }

    // Constructor with required fields
    public Sample(String name, int age, String place, String nic, Gender gender){
        this.name = name;
        this.age = age;
        this.place = place;
        this.nic = nic;
        this.gender = gender;
    }


}