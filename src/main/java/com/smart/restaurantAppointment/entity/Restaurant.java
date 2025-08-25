package com.smart.restaurantAppointment.entity;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
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

    @ManyToOne
    @JoinColumn(name="merchant_fk")
    private Merchant merchant;

    @OneToMany(mappedBy = "restaurant")
    private List<Reservation> reservations;

    private String imageLogo;

    @Enumerated(EnumType.STRING)
    private AccountStatus Status;




}
