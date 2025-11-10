package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(ReservationRequestDTO dto, User user) {
        Reservation reservation =new Reservation();

        reservation.setCustomer(user);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationDateTime(dto.getReservationDateTime());
        reservation.setSize(dto.getSize());
        return reservationRepository.save(reservation);
    }
}
