package com.smart.restaurantAppointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RestaurantAppointment {
    public static void main(String []args){
        SpringApplication.run(RestaurantAppointment.class, args);
    }
}
