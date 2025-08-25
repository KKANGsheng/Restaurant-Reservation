package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.MerchantService;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.dto.UserRegisterDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("smart")
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    MerchantService merchantService;

    @PostMapping("/doRegister")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO registerRequestDTO){
        User user =userService.register(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/merchant/register")
    public ResponseEntity <?> registerMerchant(@RequestBody MerchantRegisterDTO merchantRegisterDTO){
        Merchant merchant= merchantService.register(merchantRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(merchant);
    }

}
