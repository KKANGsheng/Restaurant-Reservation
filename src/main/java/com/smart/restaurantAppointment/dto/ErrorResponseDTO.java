package com.smart.restaurantAppointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDTO {

    private int responseCode;
    private String message;
    private LocalDateTime timestamp;
}
