package com.smart.restaurantAppointment.Exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException{
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
