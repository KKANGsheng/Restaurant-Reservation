package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.User;

public interface ReservationService {

    public Reservation createReservation(ReservationRequestDTO reservation, User user);
}
