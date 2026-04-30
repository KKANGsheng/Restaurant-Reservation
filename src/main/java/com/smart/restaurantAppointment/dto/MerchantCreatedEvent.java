package com.smart.restaurantAppointment.dto;

import lombok.Data;

@Data
public class MerchantCreatedEvent {
    private String merchantName;
    private String email;
    private String businessName;
}
