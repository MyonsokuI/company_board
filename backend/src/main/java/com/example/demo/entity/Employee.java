package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(nullable = false, length = 8)
    private String employeeNo;

    @Column(nullable = false, length = 100)
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}