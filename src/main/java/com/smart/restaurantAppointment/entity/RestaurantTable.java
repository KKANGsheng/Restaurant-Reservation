package com.smart.restaurantAppointment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Table;

@Entity
@Setter
@Getter
public class RestaurantTable extends BaseEntity{
    private String name;
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name ="restaurant_id")
    private Restaurant restaurant;
}
