package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.dto.Request.RestaurantReq;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.util.SecurityUtils;
import jakarta.persistence.*;

import java.util.List;

public interface RestaurantService {

    public List<Restaurant> getAllRestaurants();

    public Restaurant createRestaurant (RestaurantReq dto);

    public void deleteRestaurant(Long id);

    public Restaurant updateRestaurant(Long id, RestaurantReq dto);

    public List<Restaurant> getCustomerRestaurants();

    public void validateRestaurantOwnership(Restaurant restaurant);
}
