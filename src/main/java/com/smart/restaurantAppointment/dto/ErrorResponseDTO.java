package com.smart.restaurantAppointment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class ErrorResponseDTO {

    private int responseCode;
    private String message;
    private LocalDateTime timestamp;
}
