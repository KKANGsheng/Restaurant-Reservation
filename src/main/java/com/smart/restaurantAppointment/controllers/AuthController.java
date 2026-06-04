package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.AuthenticateService;
import com.smart.restaurantAppointment.Service.RefreshTokenService;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.LoginRequest;
import com.smart.restaurantAppointment.dto.LoginResponse;
import com.smart.restaurantAppointment.dto.Request.ChangePasswordReq;
import com.smart.restaurantAppointment.dto.Request.ForgotPasswordReq;
import com.smart.restaurantAppointment.dto.Request.RefreshTokenReq;
import com.smart.restaurantAppointment.dto.Request.ResetPasswordConfirmReq;
import com.smart.restaurantAppointment.dto.response.ApiResponse;
import com.smart.restaurantAppointment.dto.response.RefreshTokenResponse;
import com.smart.restaurantAppointment.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService authenticateService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticateService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshTokenReq refreshTokenReq) {
        authenticateService.logout(refreshTokenReq.refreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }

    @PostMapping("/user/refreshToken")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenReq refreshTokenReq) {
       RefreshTokenResponse refreshTokenResponse = authenticateService.refresh(refreshTokenReq.refreshToken());
       return ResponseEntity.ok(refreshTokenResponse);
    }

    @PostMapping("/password/reset")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> resetPassword (@RequestBody ChangePasswordReq changePasswordReq) {
        userService.resetPassword(changePasswordReq);
        return ResponseEntity.ok(ApiResponse.success("Password has been reset", null));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<Void>> forgotPassword (@RequestBody ForgotPasswordReq  forgotPasswordReq) {
        userService.forgotPassword(forgotPasswordReq.email());
        return ResponseEntity.ok(ApiResponse.success("Password has been reset",null));
    }

    @PostMapping("/password/reset-confirm")

    public ResponseEntity<ApiResponse<Void>> passwordResetConfirm (@RequestBody ResetPasswordConfirmReq resetPasswordConfirmReq) {
        userService.resetPasswordConfirm(resetPasswordConfirmReq);
        return ResponseEntity.ok(ApiResponse.success("Password Reset has completed",null));
    }
}
