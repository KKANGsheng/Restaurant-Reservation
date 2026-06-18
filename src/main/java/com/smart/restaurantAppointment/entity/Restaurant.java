package com.smart.restaurantAppointment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Setter
@Getter
public class Restaurant extends  BaseEntity{

    private String name;
    private String address;
    private String contactNumber;
    private Integer capacity;  // max people per time slot (null = no limit)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="merchant_fk")
    private Merchant merchant;
    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", fetch=FetchType.LAZY)
    private List<Reservation> reservations;
    private String imageLogo;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer defaultBookingMinutes;
    private Integer slotIntervalMinutes;
}
