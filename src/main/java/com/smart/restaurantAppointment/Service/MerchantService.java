package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.dto.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantService {


    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate <String,Object>kafkaTemplate;


    public Merchant register(MerchantRegisterDTO merchantRegisterDTO){
        validateMerchantRegisterRequest(merchantRegisterDTO);
        Merchant merchant =new Merchant();
        merchant.setEmail(merchantRegisterDTO.getEmail());
        merchant.setPassword(passwordEncoder.encode(merchantRegisterDTO.getPassword()));
        merchant.setStatus(AccountStatus.PENDING_ACTIVATION);
        merchant.setBusinessName(merchantRegisterDTO.getBusinessName());
        merchant.setName(merchantRegisterDTO.getName());
        merchant.setRestaurantCategory(merchantRegisterDTO.getRestaurantCategory());
        kafkaTemplate.send("merchant-created",new MerchantCreatedEvent(merchant.getBusinessName(),merchant.getEmail(),merchant.getName()));

        return merchantRepository.save(merchant);
    }

    public void validateMerchantRegisterRequest(MerchantRegisterDTO merchantRegisterDTO){
        if(merchantRepository.existsByEmail(merchantRegisterDTO.getEmail())){
            throw new IllegalArgumentException("email has been Registered");
        }

    }
}
