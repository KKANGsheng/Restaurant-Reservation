package com.smart.restaurantAppointment.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordReq(@NotBlank String oldPassword, @NotBlank @Size(min=8) String newPassword, @NotBlank String email) {
}
