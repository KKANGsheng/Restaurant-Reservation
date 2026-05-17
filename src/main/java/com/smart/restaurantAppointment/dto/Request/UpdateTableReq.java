package com.smart.restaurantAppointment.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTableReq {
    @NotBlank(message = "Table name is required")
    private String name;
    @NotNull @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;
}
