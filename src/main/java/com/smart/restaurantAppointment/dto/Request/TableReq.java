package com.smart.restaurantAppointment.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableReq {
    private String name;
    private Integer capacity;
}
