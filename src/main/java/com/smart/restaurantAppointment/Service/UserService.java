package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.User;

import java.util.List;

public interface UserService {

    User register(UserDTO userDTO);
    User resetPassword(UserDTO userDTO, Long id);
    List<User> getAllUser();

}
