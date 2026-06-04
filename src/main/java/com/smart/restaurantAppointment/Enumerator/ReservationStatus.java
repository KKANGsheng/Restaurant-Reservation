package com.smart.restaurantAppointment.Enumerator;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    PENDING_CONFIRMATION ("Pending Confirmation"),
    CONFIRMED ("Confirmed"),
    CANCELED ("Canceled");
    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}
