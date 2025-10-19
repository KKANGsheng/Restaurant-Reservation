package com.smart.restaurantAppointment.dto;


import lombok.Data;

@Data
public class BookingCreatedEvent {
    private String refId;
    private String customerName;
    private String Email;
}
