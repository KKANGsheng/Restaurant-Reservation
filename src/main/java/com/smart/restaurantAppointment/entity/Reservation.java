package com.smart.restaurantAppointment.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Setter
@Getter
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    private LocalDateTime reservationDateTime;

    private int size;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="assigned_table_id")
    private RestaurantTable assignedTable;

    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private Boolean reminder;
}
