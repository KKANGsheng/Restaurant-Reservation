package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table,Long> {

    List<Table> findByRestaurant(Restaurant restaurant);
}
