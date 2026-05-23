package com.smart.restaurantAppointment.dto.Request;

import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class RestaurantReq {
    @NotBlank (message = "Restaurant name is required")
    private String name;
    @NotBlank (message = "Address is required")
    private String address;
    private String contactNumber;
    @NotNull (message = "Capacity is required")
    @Min     (value = 1, message = "Capacity must be at least 1")
    private Integer capacity;  // max people per time slot
    @NotNull
    private LocalTime openingTime;
    @NotNull
    private LocalTime closingTime;
    @NotNull
    private Integer   slotIntervalMinutes;
    @NotNull
    private Integer   defaultBookingMinutes;
    @NotNull
    private RestaurantCategory restaurantCategory;
}
