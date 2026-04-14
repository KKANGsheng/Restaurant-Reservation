package com.smart.restaurantAppointment.dto.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRegistrationReq {
    private String email;
    private String password;
    private String token;
}
