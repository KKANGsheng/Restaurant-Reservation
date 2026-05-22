package com.smart.restaurantAppointment.util;

import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Exception.ForbiddenException;
import com.smart.restaurantAppointment.Exception.NotFoundException;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.UserRepository;
import com.smart.restaurantAppointment.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;

    private AuthenticatedUser current() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Merchant getCurrentMerchant() {
        AuthenticatedUser principal = current();

        if (principal.role() !=UserRole.MERCHANT) {
            throw new ForbiddenException("it is not merchant");
        }

        return merchantRepository.findById(principal.id()).orElseThrow(()->new NotFoundException("merchant not found"));
    }

    public User getCurrentUser() {
        AuthenticatedUser principal = current();

        if (principal.role() != UserRole.CUSTOMER) {
            throw new ForbiddenException("it is not a customer");
        }
        return userRepository.findById(principal.id()).orElseThrow(()-> new NotFoundException("user not found"));
    }

}
