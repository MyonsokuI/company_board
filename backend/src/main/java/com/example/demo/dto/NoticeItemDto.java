package com.example.demo.dto;

import java.time.LocalDate;

public record NoticeItemDto(
        Integer noticeId,
        String title,
        String body,
        Integer priority,
        LocalDate publishedUntil,
        String createdAt,
        String createUserName) {
}