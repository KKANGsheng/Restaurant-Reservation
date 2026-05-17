package com.smart.restaurantAppointment.Exception;


import org.springframework.http.HttpStatus;

public abstract  class AppException extends RuntimeException{
    private final HttpStatus status;

    public AppException (HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
