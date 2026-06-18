package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.RestaurantDTO;
import com.smart.restaurantAppointment.dto.request.RestaurantReq;
import com.smart.restaurantAppointment.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    public List<RestaurantDTO> getAllRestaurants();

    public Restaurant createRestaurant (RestaurantReq dto);

    public void deleteRestaurant(Long id);

    public Restaurant updateRestaurant(Long id, RestaurantReq dto);

    public List<Restaurant> getCustomerRestaurants();

    public void validateRestaurantOwnership(Restaurant restaurant);
}
