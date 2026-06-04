package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.dto.response.PageResponse;
import com.smart.restaurantAppointment.dto.response.ReservationResponseDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO, User user);

    public void validateReservation(Restaurant restaurant, User user, ReservationRequestDTO dto);

    Page<ReservationResponseDTO> getCustomerReservations(Pageable pageable);

    PageResponse<ReservationResponseDTO> getMerchantReservations(Pageable pageable, LocalDate fromDate, LocalDate toDate, ReservationStatus reservationStatus, String customerName, String email, Long restaurantId);

    ReservationResponseDTO cancelReservation(Long id);

    ReservationResponseDTO confirmReservation(Long id);

    PageResponse<ReservationResponseDTO> getReservationHistory(Pageable pageable);
}
