package com.smart.restaurantAppointment.repository;


import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation,Long> {
    Page<Reservation> findByCustomer(User customer, Pageable pageable);
    Page<Reservation> findByRestaurantIn(List<Restaurant> restaurants, Pageable pageable);

}
