package com.smart.restaurantAppointment.dto;


import lombok.Data;

@Data
public class UserCreatedEvent {

    private String userId;
    private String name;
    private String email;
}
