package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public UserDetails Authenticate(LoginRequest input){
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

}
