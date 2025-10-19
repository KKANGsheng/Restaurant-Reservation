package com.smart.restaurantAppointment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class MerchantCreatedEvent {

    private String merchantName;
    private String authoriserName;
    private String email;
}
