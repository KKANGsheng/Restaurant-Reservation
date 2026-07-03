package com.smart.restaurantAppointment.dto;

import com.smart.restaurantAppointment.entity.RestaurantTable;
import lombok.Data;

@Data
public class RestaurantTableDTO {
    private String name;
    private Integer capacity;
    private Long restaurantId;

    public static RestaurantTableDTO from (RestaurantTable table) {
        RestaurantTableDTO dto = new RestaurantTableDTO();
        dto.setName(table.getName());
        dto.setCapacity(table.getCapacity());
        dto.setRestaurantId(table.getRestaurant().getId());
        return dto;
    }

}
