package com.smart.restaurantAppointment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserRegisterDTO {


    @Email
    private String email;

    @NotBlank
    private String password;

    private Long merchantId;
}
