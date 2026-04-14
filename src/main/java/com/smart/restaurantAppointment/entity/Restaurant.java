package com.smart.restaurantAppointment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Restaurant extends  BaseEntity{

    private String name;
    private String address;
    private String contactNumber;
    private Integer capacity;  // max people per time slot (null = no limit)
    @ManyToOne
    @JoinColumn(name="merchant_fk")
    private Merchant merchant;
    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private List<Reservation> reservations;
    private String imageLogo;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;
}
