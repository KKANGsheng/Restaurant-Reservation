package com.smart.restaurantAppointment.repository;


import com.smart.restaurantAppointment.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation,Long> {
}
