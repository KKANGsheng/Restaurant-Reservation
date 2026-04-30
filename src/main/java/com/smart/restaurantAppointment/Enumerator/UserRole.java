package com.smart.restaurantAppointment.Enumerator;

import lombok.Getter;

@Getter
public enum UserRole {
    MERCHANT("Merchant"),
    CUSTOMER("Customer"),
    MERCHANT_ADMIN("Merchant Admin");

    private final String description;
    UserRole(String description) {
        this.description = description;
    }
}
