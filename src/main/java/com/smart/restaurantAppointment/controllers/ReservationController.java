package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.dto.response.PageResponse;
import com.smart.restaurantAppointment.dto.response.ReservationResponseDTO;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
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
    public ResponseEntity<PageResponse<ReservationResponseDTO>> getAllReservations(
            @RequestParam(required = false) ReservationStatus reservationStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long restaurantId,
            @PageableDefault(size = 10, sort = "createdDate") Pageable pageable
    ) {
        return ResponseEntity.ok(reservationService.getMerchantReservations(pageable,fromDate, toDate, reservationStatus, name,email,restaurantId));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public PageResponse<ReservationResponseDTO> getReservationHistory(Pageable pageable) {
        return reservationService.getReservationHistory(pageable);
    }

}
