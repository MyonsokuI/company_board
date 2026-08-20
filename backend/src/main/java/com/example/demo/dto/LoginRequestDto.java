package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String loginId;
    private String password;
}