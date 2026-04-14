package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.MerchantService;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.Service.impl.UserServiceImpl;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.dto.Request.InviteRegistrationReq;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    UserService userService;
    @Autowired
    MerchantService merchantService;

    @PostMapping("/merchant")
    public ResponseEntity <?> registerMerchant(@Valid @RequestBody MerchantRegisterDTO merchantRegisterDTO) {
        Merchant merchant= merchantService.register(merchantRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(merchant);
    }

//  Basically flow is sent to user and let them manually register under this merchant
    @PostMapping("/user/invite")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity <?> inviteUser(@Valid @RequestBody InviteRegistrationReq req) {
        UserDTO user = userService.registerViaInvite(req.getEmail(), req.getPassword(), req.getToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
