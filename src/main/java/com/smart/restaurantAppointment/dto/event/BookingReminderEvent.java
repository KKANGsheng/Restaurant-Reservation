package com.smart.restaurantAppointment.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingReminderEvent {
    private String customerName;
    private String email;
    private LocalDateTime bookingTime;
}
