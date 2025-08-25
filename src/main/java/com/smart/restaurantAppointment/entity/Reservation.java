package com.smart.restaurantAppointment.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Reservation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    private LocalDateTime reservationDateTime;

    private int size;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


}
