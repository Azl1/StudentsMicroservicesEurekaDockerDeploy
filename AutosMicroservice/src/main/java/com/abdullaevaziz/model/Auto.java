package com.abdullaevaziz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "autos" , uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "power", "year","student_id"})})
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String brand;

    private int power;

    private int year;

    @Column(name = "student_id", nullable = false)
    private long studentId;
}
