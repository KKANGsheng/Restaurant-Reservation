package com.smart.restaurantAppointment.entity;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.security.AppPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MerchantUserDetails implements AppPrincipal {

    private final Merchant merchant;

    public MerchantUserDetails(Merchant merchant) {
        this.merchant = merchant;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(UserRole.MERCHANT.name()));
    }

    @Override
    public String getPassword() {
        return merchant.getPassword();
    }

    @Override
    public String getUsername() {
        return merchant.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return merchant.getStatus() == AccountStatus.ACTIVE;
    }

    @Override
    public Long getId() {
        return merchant.getId();
    }

    @Override
    public UserRole getRole() {
        return UserRole.MERCHANT;
    }

}
