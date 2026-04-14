package com.smart.restaurantAppointment.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestaurantReq {
    private String name;
    private String address;
    private String contactNumber;
    private Integer capacity;  // max people per time slot
}
