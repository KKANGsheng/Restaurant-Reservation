package com.smart.restaurantAppointment.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreatedEvent {
    private Long refId;
    private String customerName;
    private String Email;
    private String restaurantName;
    private LocalDateTime reservationDateTime;
    private int size;
}
