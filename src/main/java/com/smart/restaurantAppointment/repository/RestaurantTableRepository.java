package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable,Long> {

    List<RestaurantTable> findByRestaurant(Restaurant restaurant);
}
