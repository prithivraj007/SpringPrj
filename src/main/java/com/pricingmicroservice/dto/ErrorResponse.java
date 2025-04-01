package com.pricingmicroservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ErrorResponse {
    private String type;
    private String title;
    private int status;
    private String detail;
    private LocalDateTime timestamp;

    public ErrorResponse(String type, String title, int status, String detail, LocalDateTime timestamp) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.timestamp = timestamp;
    }
}
