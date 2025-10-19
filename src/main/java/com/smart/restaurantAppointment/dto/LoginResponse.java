package com.smart.restaurantAppointment.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class LoginResponse {

    private String token;
    private String role;
    private String email;
    private String password;
    private long expiresIn;

}