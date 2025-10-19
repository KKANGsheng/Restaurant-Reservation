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
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<User> resetPassword(@RequestBody UserDTO userdto, @PathVariable Long id){
        User user=userService.resetPassword(userdto,id);
        return   ResponseEntity.ok(user);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity <List<User>> getAllUser(){
        List<User> allUser=userService.getAllUser();
        return new ResponseEntity<>(allUser,HttpStatus.ACCEPTED);
    }
}
