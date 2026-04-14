package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.config.AppConfig;
import com.smart.restaurantAppointment.dto.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.entity.InviteToken;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.InviteTokenRepository;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public interface MerchantService {
    Merchant register(MerchantRegisterDTO merchantRegisterDTO);
    String generateInviteUrlLink();

}
