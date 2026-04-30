package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.RefreshToken;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {
    public RefreshToken createUserRefreshToken(User user);

    public RefreshToken createMerchantRefreshToken(Merchant merchant);

    public String issueRefreshToken(UserDetails userDetails);
}
