package com.smart.restaurantAppointment.security;

import com.smart.restaurantAppointment.Enumerator.UserRole;

public record AuthenticatedUser(Long id, String email, UserRole role) {

}
