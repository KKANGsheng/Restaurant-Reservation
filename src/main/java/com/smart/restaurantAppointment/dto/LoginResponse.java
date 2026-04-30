package com.smart.restaurantAppointment.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class LoginResponse {

    private String accessToken;
    private String role;
    private String email;
    private long expiresIn;
    private String refreshToken;

}