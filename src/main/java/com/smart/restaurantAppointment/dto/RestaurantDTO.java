package com.smart.restaurantAppointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Data
public class RestaurantDTO {

    private String name;
    private String address;
    private String contactNumber;
    private Integer capacity;
    private String imageLogo;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer defaultBookingMinutes;
    private Integer slotIntervalMinutes;

    public static RestaurantDTO from (Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getContactNumber(),
                restaurant.getCapacity(),
                restaurant.getImageLogo(),
                restaurant.getRestaurantCategory(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                restaurant.getDefaultBookingMinutes(),
                restaurant.getSlotIntervalMinutes());
    }
}
