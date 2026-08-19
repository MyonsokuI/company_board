package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NoticeCreateRequestDto {
    private String title;
    private String body;
    private Integer priority;
    private LocalDate publishedUntil;
    private Integer createdBy;
}