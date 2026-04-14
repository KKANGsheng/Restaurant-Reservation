package com.smart.restaurantAppointment.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.base-url}")
    public String baseUrl;
}
