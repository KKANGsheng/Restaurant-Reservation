package com.smart.restaurantAppointment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableReq {
    @NotBlank
    private String name;
    @NotNull @Min(1)
    private Integer capacity;
    @NotNull
    private Long restaurantId;
}
