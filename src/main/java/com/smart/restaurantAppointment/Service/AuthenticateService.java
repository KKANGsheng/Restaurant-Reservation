package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.dto.LoginRequest;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.UserRepository;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


//    Authenticate will call loadUserByName function
//    authenticate will return authentication Object
//    which consist of pricipal,authorities and credentials

    public MyUserDetails Authenticate(LoginRequest input){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );


        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        // get the full User entity without querying again
        return myUserDetails;
    }

}
