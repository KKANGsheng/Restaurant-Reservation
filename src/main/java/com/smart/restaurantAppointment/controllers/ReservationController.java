package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequestDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Reservation reservation=  reservationService.createReservation(dto, user);
         return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
}
