package com.mhoch.githubuser.domain.dto;

import lombok.Data;

@Data
public class ErrorDto {
    String status;
    String message;
}
