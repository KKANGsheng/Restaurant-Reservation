package com.smart.restaurantAppointment.controllers;

import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
public class UserController {

    @Autowired
    private UserService userService;
}
