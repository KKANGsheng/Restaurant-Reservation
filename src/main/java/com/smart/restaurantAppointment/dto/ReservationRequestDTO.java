package com.smart.restaurantAppointment.dto;


import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "RestaurantId is Required")
    private Long restaurantId;

    @NotNull(message = "Reservation date time is required")
    private LocalDateTime reservationDateTime;

    @Min(value =1, message = "size must be at least 1")
    private int size;
}
