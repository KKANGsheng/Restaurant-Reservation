package com.smart.restaurantAppointment.Enumerator;


import lombok.Getter;

@Getter
public enum RestaurantCategory {
    FASTFOOD ("Fastfood"),
    WESTERN ("Western"),
    EASTERN ("Eastern"),
    CHINESE ("Chinese"),
    JAPANESE ("Japanese"),
    KOREAN ("Korean");
    private final String description;
    RestaurantCategory(String description) {
        this.description = description;
    }

}
