package com.smart.restaurantAppointment.dto;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.RestaurantCategory;
import com.smart.restaurantAppointment.Enumerator.SubscriptionPlan;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantRegisterDTO {

    private String name;
    private String email;
    private String businessName;
    private String address;
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private RestaurantCategory restaurantCategory;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;


}
