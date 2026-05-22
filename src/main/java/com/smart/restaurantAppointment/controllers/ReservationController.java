package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.dto.response.ReservationResponseDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final SecurityUtils securityUtils;
    public ReservationController(ReservationService reservationService, SecurityUtils securityUtils) {
        this.reservationService = reservationService;
        this.securityUtils = securityUtils;
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO dto) {
        User user = securityUtils.getCurrentUser();
        ReservationResponseDTO reservation = reservationService.createReservation(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/getCustomerReservation")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Page<ReservationResponseDTO>> getCustomerReservations( @PageableDefault(size = 10, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(reservationService.getCustomerReservations(pageable));
    }


    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable Long id) {
        ReservationResponseDTO reservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<ReservationResponseDTO> confirmReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Page<ReservationResponseDTO>> getAllReservations(@PageableDefault(size = 10, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(reservationService.getMerchantReservations(pageable));
    }
}
