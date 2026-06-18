package com.smart.restaurantAppointment.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetRequestedEvent {
    private String email;
    private String token;
    private LocalDateTime expiresAt;
    private String purpose;
    private String name;
}
