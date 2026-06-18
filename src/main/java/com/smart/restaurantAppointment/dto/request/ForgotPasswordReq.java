package com.smart.restaurantAppointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordReq(@NotBlank String email, @NotNull String token) {
}
