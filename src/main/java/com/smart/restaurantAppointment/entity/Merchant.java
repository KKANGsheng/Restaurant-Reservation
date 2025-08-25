package com.smart.restaurantAppointment.entity;

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
    private String businessName;
    private String address;
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;

//  Lookup at the merchant field in restaurant
//  Restaurant store fk
    @OneToMany(mappedBy = "merchant")
    private List<Restaurant> restaurantList;

//  User store fk
    @OneToMany(mappedBy = "merchant")
    private List<User>customers;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private String stripeCustomerId;

    private String stripeSubscriptionId;



}
