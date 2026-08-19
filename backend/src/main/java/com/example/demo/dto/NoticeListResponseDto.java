package com.example.demo.dto;

import java.util.List;

public record NoticeListResponseDto(
        List<NoticeItemDto> notices) {
}