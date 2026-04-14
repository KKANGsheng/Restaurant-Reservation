package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.UserCreatedEvent;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.InviteToken;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;

import java.util.List;

public interface UserService {

    UserDTO register(UserDTO userDTO);
    UserDTO resetPassword(UserDTO userDTO);

    UserDTO packageResponseDTO(User user, Merchant merchant);

    InviteToken validateInviteToken(String Token);

    UserDTO registerViaInvite(String email, String password, String token);

    void incrementFailedAttempts(String email);

    void resetFailedAttempts(String email);
}
