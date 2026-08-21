package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private Integer priority; // または @ManyToOne で Priority エンティティと紐付け

    @Column(name = "created_by", nullable = false)
    private Integer createdBy; // 社員IDをそのまま持つ場合

    private LocalDate publishedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}