package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.RefreshTokenService;
import com.smart.restaurantAppointment.entity.*;
import com.smart.restaurantAppointment.repository.RefreshTokenRepository;
import com.smart.restaurantAppointment.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createUserRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken createMerchantRefreshToken(Merchant merchant) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMerchant(merchant);
        refreshToken.setRevoked(false);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public String issueRefreshToken(UserDetails userDetails) {
        if (userDetails instanceof MyUserDetails user) {
            return createUserRefreshToken(user.getUser()).getToken();
        }else if (userDetails instanceof MerchantUserDetails merchant) {
            return createMerchantRefreshToken(merchant.getMerchant()).getToken();
        }else{
            throw new IllegalStateException("Unsupported principal type: "+ userDetails.getClass().getName());
        }
    }

    @Override
    public RefreshToken rotate(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        if (oldToken.getUser()!=null) {
            return  createUserRefreshToken(oldToken.getUser());
        } else if (oldToken.getMerchant()!=null) {
            return  createMerchantRefreshToken(oldToken.getMerchant());
        }
        throw new IllegalStateException("refreshToken did not Exist");
    }

}
