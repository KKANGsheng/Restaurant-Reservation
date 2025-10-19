package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.AuthenticateService;
import com.smart.restaurantAppointment.dto.LoginRequest;
import com.smart.restaurantAppointment.dto.LoginResponse;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.security.auth.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService authenticateService;

    private final JwtService jwtService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest loginRequest){

        MyUserDetails authenticateUser=authenticateService.Authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(authenticateUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);

    }

}
