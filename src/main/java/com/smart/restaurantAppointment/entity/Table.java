package com.smart.restaurantAppointment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Table extends BaseEntity{
    private String name;
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name ="restaurant_id")
    private Restaurant restaurant;
}
