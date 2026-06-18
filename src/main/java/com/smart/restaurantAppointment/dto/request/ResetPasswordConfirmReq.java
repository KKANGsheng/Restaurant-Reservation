package com.smart.restaurantAppointment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordConfirmReq (@NotBlank String token,@NotBlank String newPassword) {
}
