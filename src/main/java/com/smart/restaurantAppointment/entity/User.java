package com.smart.restaurantAppointment.entity;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.support.ActiveObjectCounter;

@Entity
@Setter
@Getter
public class User extends  BaseEntity {
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole Role;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;


    @ManyToOne
    @JoinColumn(name="merchant_id")
    private Merchant merchant;



}
