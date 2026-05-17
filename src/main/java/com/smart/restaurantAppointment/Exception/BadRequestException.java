package com.smart.restaurantAppointment.Exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException{

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
