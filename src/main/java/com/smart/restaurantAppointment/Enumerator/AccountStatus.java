package com.smart.restaurantAppointment.Enumerator;


import lombok.Getter;

@Getter
public enum AccountStatus {
    PENDING_ACTIVATION("Pending Activation"),
    SUSPENDED("Suspended"),
    ACTIVE("Active"),
    UNDER_REVIEW("Under Review"),
    TERMINATED("Terminated");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }
}
