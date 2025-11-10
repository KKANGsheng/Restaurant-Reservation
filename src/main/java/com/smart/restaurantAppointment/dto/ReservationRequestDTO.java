package com.smart.restaurantAppointment.dto;


import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {

    private Long restaurantId;

    private LocalDateTime reservationDateTime;

    private int size;
}
