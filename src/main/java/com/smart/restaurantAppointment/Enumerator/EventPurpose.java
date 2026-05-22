package com.smart.restaurantAppointment.Enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventPurpose {
    RESET_PASSWORD      ("Reset Password"),
    NEW_USER_INVITE     ("New User Invite");
    private final String description;
}
