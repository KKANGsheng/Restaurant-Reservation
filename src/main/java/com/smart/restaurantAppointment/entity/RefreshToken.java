package com.smart.restaurantAppointment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RefreshToken extends BaseEntity {

    private String token;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name ="merchant_id")
    private Merchant merchant;
}
