package com.smart.restaurantAppointment.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RestaurantTable extends BaseEntity{
    private String name;
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="restaurant_id")
    private Restaurant restaurant;
}
