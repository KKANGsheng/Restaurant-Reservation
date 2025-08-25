package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.dto.UserRegisterDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MerchantService {

    @Autowired
    MerchantRepository merchantRepository;

    public Merchant register(MerchantRegisterDTO merchantRegisterDTO){
        Merchant merchant =new Merchant();
        merchant.setEmail(merchantRegisterDTO.getEmail());
        merchant.setStatus(AccountStatus.PENDING_ACTIVATION);
        merchant.setBusinessName(merchantRegisterDTO.getBusinessName());
        merchant.setName(merchantRegisterDTO.getName());
        merchant.setRestaurantCategory(merchantRegisterDTO.getRestaurantCategory());
        return merchantRepository.save(merchant);
    }
}
