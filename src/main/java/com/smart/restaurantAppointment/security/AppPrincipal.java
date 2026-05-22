package com.smart.restaurantAppointment.security;

import com.smart.restaurantAppointment.Enumerator.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

public interface AppPrincipal extends UserDetails {
    Long getId();
    UserRole getRole();
}
