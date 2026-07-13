package com.smart.restaurantAppointment.Enumerator;

import lombok.Getter;

@Getter
public enum SubscriptionTier {
    FREE        ("Weekly"),
    PRO         ("Monthly"),
    ENTERPRISE  ("Yearly");

    private final String description;

    SubscriptionTier(String description) {
        this.description = description;
    }

}
