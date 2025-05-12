package com.example.schedule.model;

import java.time.LocalDateTime;

public class ErrorResponse extends RuntimeException {
    private String message;       // Сообщение об ошибке
    private LocalDateTime timestamp;  // Время возникновения ошибки

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
