package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.MerchantService;
import com.smart.restaurantAppointment.config.AppConfig;
import com.smart.restaurantAppointment.dto.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.entity.InviteToken;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.repository.InviteTokenRepository;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final InviteTokenRepository inviteTokenRepository;
    private final AppConfig appConfig;
    private final RestaurantRepository restaurantRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public Merchant register(MerchantRegisterDTO merchantRegisterDTO) {
        validateMerchantRegisterRequest(merchantRegisterDTO);
//      Register merchant
        Merchant merchant =new Merchant();
        merchant.setEmail(merchantRegisterDTO.getEmail());
        merchant.setPassword(passwordEncoder.encode(merchantRegisterDTO.getPassword()));
        merchant.setStatus(AccountStatus.ACTIVE);
        merchant.setBusinessName(merchantRegisterDTO.getBusinessName());
        merchant.setName(merchantRegisterDTO.getName());
        Merchant m =merchantRepository.save(merchant);

//      register restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(merchant.getAddress());
        restaurant.setName(merchantRegisterDTO.getRestaurantName());
        restaurant.setStatus(AccountStatus.ACTIVE);
        restaurant.setRestaurantCategory(merchantRegisterDTO.getRestaurantCategory());
        restaurant.setMerchant(m);
        restaurantRepository.save(restaurant);
//      publish event
        MerchantCreatedEvent event =  new MerchantCreatedEvent();
        event.setMerchantName(m.getName());
        event.setEmail(m.getEmail());
        event.setBusinessName(m.getBusinessName());
        eventPublisher.publishEvent(event);
        return m;
    }

    public void validateMerchantRegisterRequest(MerchantRegisterDTO merchantRegisterDTO){
        if(merchantRepository.existsByEmail(merchantRegisterDTO.getEmail())){
            throw new IllegalArgumentException("email has been Registered");
        }
    }

    @Override
    public String generateInviteUrlLink() {
        Merchant merchant = securityUtils.getCurrentMerchant();
        String token = UUID.randomUUID().toString();
        InviteToken inviteToken = new InviteToken();
        inviteToken.setMerchant(merchant);
        inviteToken.setToken(token);
        inviteToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        inviteTokenRepository.save(inviteToken);
        String url = UriComponentsBuilder
                .fromHttpUrl(appConfig.baseUrl)
                .path("/register/user/invite")
                .queryParam("invite", token)
                .toUriString();
        return url;
    }

}
