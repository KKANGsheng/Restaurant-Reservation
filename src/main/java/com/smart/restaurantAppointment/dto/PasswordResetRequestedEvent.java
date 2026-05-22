package com.smart.restaurantAppointment.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class PasswordResetRequestedEvent {
    private String email;
    private String token;
    private LocalDateTime expiresAt;
    private String purpose;
    private String name;
}
