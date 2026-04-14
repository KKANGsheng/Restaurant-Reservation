package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.dto.response.ReservationResponseDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO, User user);

    public void validateReservation(Restaurant restaurant, User user, ReservationRequestDTO dto);

    Page<ReservationResponseDTO> getCustomerReservations(Pageable pageable);

    Page<ReservationResponseDTO> getMerchantReservations(Pageable pageable);

    ReservationResponseDTO cancelReservation(Long id);

    ReservationResponseDTO confirmReservation(Long id);
}
