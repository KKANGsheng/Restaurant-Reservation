package com.smart.restaurantAppointment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatedUserByMerchantReq {

    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;
}
