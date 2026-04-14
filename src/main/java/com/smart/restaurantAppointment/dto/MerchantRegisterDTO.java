package com.smart.restaurantAppointment.dto;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import com.smart.restaurantAppointment.Enumerator.SubscriptionPlan;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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
    private String address;
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;
    private String restaurantName;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private String password;



}
