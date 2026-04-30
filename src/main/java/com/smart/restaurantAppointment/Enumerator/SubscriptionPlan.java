package com.smart.restaurantAppointment.Enumerator;


import lombok.Getter;

@Getter
public enum SubscriptionPlan {

    WEEKLY ("Weekly"),
    MONTHLY ("Monthly"),
    YEARLY ("Yearly");

    private final String description;

    SubscriptionPlan(String description) {
        this.description = description;
    }
}
