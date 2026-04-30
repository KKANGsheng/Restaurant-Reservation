package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import org.springframework.stereotype.Service;

@Service
public interface MerchantService {
    Merchant register(MerchantRegisterDTO merchantRegisterDTO);
    String generateInviteUrlLink();

}
