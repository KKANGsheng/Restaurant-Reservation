package com.smart.restaurantAppointment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    @JoinColumn(name="merchant_id")
    private Merchant merchant;
    private Boolean accountLocked;
    private int failedAttempts;
    private String phoneNumber;
    private String name;
}
