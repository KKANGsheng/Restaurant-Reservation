package com.smart.restaurantAppointment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import com.smart.restaurantAppointment.Enumerator.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.IdGeneratorType;

import java.util.List;

@Entity
@Setter
@Getter
public class Merchant extends BaseEntity {

    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String businessName;
    private String address;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
//  Lookup at the merchant field in restaurant
//  Restaurant store fk
    @JsonIgnore
    @OneToMany(mappedBy = "merchant")
    private List<Restaurant> restaurantList;

//  User store fk
    @JsonIgnore
    @OneToMany(mappedBy = "merchant")
    private List<User>customers;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private String stripeCustomerId;

    private String stripeSubscriptionId;
}
