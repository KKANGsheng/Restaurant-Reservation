package com.smart.restaurantAppointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class InviteToken extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private String token;

    private LocalDateTime expiryDate;
}
