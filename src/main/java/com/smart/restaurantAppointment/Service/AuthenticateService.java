package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.dto.LoginRequest;
import com.smart.restaurantAppointment.dto.LoginResponse;
import com.smart.restaurantAppointment.dto.response.RefreshTokenResponse;
import com.smart.restaurantAppointment.entity.MerchantUserDetails;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.RefreshToken;
import com.smart.restaurantAppointment.jwt.JwtService;
import com.smart.restaurantAppointment.repository.RefreshTokenRepository;
import com.smart.restaurantAppointment.security.AppPrincipal;
import com.smart.restaurantAppointment.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserDetails authenticate(LoginRequest input) {
        try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );
        userService.resetFailedAttempts(input.email());
        return (UserDetails) authentication.getPrincipal();
    } catch (LockedException Exception) {
            throw new BadRequestException("Your account is locked. Please contact support");
    } catch (BadCredentialsException exception) {
         userService.incrementFailedAttempts(input.email());
         throw new BadRequestException("Invalid email or password");
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        //At here will either return refreshToken and accessToken
        AppPrincipal appPrincipal = (AppPrincipal) authenticate(loginRequest);
        String jwtToken = jwtService.generateJwtToken(appPrincipal.getId(),appPrincipal.getUsername(), appPrincipal.getRole().name());
        String refreshToken = refreshTokenService.issueRefreshToken(appPrincipal);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setEmail(appPrincipal.getUsername());
        loginResponse.setRole(appPrincipal.getAuthorities().iterator().next().getAuthority());
        loginResponse.setRefreshToken(refreshToken);
        return loginResponse;
    }

    public void logout(String refreshToken) {
      RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Refresh token not found"));
      if (token.isRevoked()) {
          throw new BadRequestException("Token has been revoked");
      }
      token.setRevoked(Boolean.TRUE);
      refreshTokenRepository.save(token);
    }

    @Transactional
    public RefreshTokenResponse refresh (String refreshToken) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Refresh token not found"));
        if (oldToken.isRevoked()) {
            throw new BadRequestException("Token has been revoked");
        }
        if (oldToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token has Expired");
        }
        RefreshToken newToken = refreshTokenService.rotate(oldToken);
        AppPrincipal appPrincipal = (AppPrincipal) newToken.getUser()!=null ?
                                    new MyUserDetails(newToken.getUser())
                                    : new MerchantUserDetails(newToken.getMerchant());
        String accessToken = jwtService.generateJwtToken(appPrincipal.getId(), appPrincipal.getUsername(), appPrincipal.getRole().name());
        return new RefreshTokenResponse(newToken.getToken(),accessToken);
    }

}
