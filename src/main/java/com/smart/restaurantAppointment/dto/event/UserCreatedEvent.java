package com.smart.restaurantAppointment.dto.event;

import lombok.Data;

@Data
public class UserCreatedEvent {
    private String email;
    private String role;
    private String status;
}
