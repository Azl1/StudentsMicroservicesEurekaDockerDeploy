package com.abdullaevaziz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fio;

    private int age;

    @Column(unique = true)
    private int num;

    private double salary;

    @Transient
    private List<Auto> autos = new ArrayList<>();
}
