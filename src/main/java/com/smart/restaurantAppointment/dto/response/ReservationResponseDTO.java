package com.smart.restaurantAppointment.dto.response;

import com.smart.restaurantAppointment.entity.Reservation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime reservationDateTime;
    private int size;
    private String status;
    private LocalDateTime createdDate;

    public static ReservationResponseDTO from (Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setRestaurantId(reservation.getRestaurant().getId());
        dto.setRestaurantName(reservation.getRestaurant().getName());
        dto.setReservationDateTime(reservation.getReservationDateTime());
        dto.setSize(reservation.getSize());
        dto.setStatus(reservation.getStatus().getDescription());
        return dto;
    }

}
