package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.AuthenticateService;
import com.smart.restaurantAppointment.Service.RefreshTokenService;
import com.smart.restaurantAppointment.dto.LoginRequest;
import com.smart.restaurantAppointment.dto.LoginResponse;
import com.smart.restaurantAppointment.dto.Request.RefreshTokenRequest;
import com.smart.restaurantAppointment.dto.response.ApiResponse;
import com.smart.restaurantAppointment.dto.response.RefreshTokenResponse;
import com.smart.restaurantAppointment.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService authenticateService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticateService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        authenticateService.logout(refreshTokenRequest.refreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }

    @PostMapping("/user/refreshToken")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
       RefreshTokenResponse refreshTokenResponse = authenticateService.refresh(refreshTokenRequest.refreshToken());
       return ResponseEntity.ok(refreshTokenResponse);
    }

}
