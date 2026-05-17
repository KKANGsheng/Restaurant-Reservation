package com.smart.restaurantAppointment.repository;


import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.Table;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation,Long> {
    Page<Reservation> findByCustomer(User customer, Pageable pageable);
    Page<Reservation> findByRestaurantIn(List<Restaurant> restaurants, Pageable pageable);
    @Query("SELECT r from Reservation r " +
            "WHERE r.assignedTable = :table " +
            "AND r.reservationDateTime <:end " +
            "AND r.endDateTime >:start " +
            "AND r.status <> :excludedStatus ")
    List<Reservation> findOverlappingRestaurant (@Param("table") Table table, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("excludedStatus")ReservationStatus excludedStatus);
}
