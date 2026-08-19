package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "priorities")
@Data
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer priorityId;

    @Column(nullable = false, unique = true, length = 5)
    private String priority;
}