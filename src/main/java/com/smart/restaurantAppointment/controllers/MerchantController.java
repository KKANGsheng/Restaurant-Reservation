package com.smart.restaurantAppointment.controllers;

import com.smart.restaurantAppointment.Service.MerchantService;
import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.request.CreatedUserByMerchantReq;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.dto.response.InviteLinkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final ReservationService reservationService;

    @PostMapping("/invite-link")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<?> generateInviteLink() {
        String url = merchantService.generateInviteUrlLink();
        return ResponseEntity.ok(new InviteLinkResponse(url));
    }

    //  Merchant can manually register user
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreatedUserByMerchantReq registerReq) {
        UserDTO user = userService.register(registerReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}



