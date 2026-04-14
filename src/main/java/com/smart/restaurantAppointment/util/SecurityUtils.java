package com.smart.restaurantAppointment.util;

import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.MerchantUserDetails;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Merchant getCurrentMerchant() {
        System.out.println();

        System.out.println("principal " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        System.out.println();

        MerchantUserDetails merchantDetails = (MerchantUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return merchantDetails.getMerchant();
    }

    public static User getCurrentUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

}
