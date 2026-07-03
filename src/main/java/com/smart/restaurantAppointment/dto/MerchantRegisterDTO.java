package com.smart.restaurantAppointment.dto;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import com.smart.restaurantAppointment.Enumerator.SubscriptionPlan;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class MerchantRegisterDTO {

    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String businessName;
    @NotBlank
    private String address;
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;
    @NotBlank
    private String restaurantName;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    @NotBlank
    private String password;

    @NotBlank
    private String slug;
    private String logo;
    @NotBlank
    private String brandColor;

    @NotNull
    private LocalTime openingTime;
    @NotNull
    private LocalTime closingTime;
    @NotNull
    private Integer   slotIntervalMinutes;
}
