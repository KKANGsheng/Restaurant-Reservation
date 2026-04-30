package com.smart.restaurantAppointment.Service.impl;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.UserCreatedEvent;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.InviteToken;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.InviteTokenRepository;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.UserRepository;
import com.smart.restaurantAppointment.util.SecurityUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MerchantRepository merchantRepository;
    private final InviteTokenRepository inviteTokenRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UserDTO register(UserDTO register){
        Merchant merchant = SecurityUtils.getCurrentMerchant();
        return createUser(register.getEmail(), register.getPassword(), merchant);
    }

    public UserDTO registerViaInvite(String email, String password, String token) {
        InviteToken inviteToken = validateInviteToken(token);
        Merchant merchant = inviteToken.getMerchant();

        return createUser(email, password, merchant);
    }

    @Override
    public void incrementFailedAttempts(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
                int attempts = user.getFailedAttempts() + 1;
                user.setFailedAttempts(attempts);
                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    user.setAccountLocked(true);
                }
            userRepository.save(user);
            });
    }

    private UserDTO createUser(String email, String password, Merchant merchant) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new BadRequestException("Email and password are required");
        }

        if (userRepository.existsByEmailAndMerchantId(email, merchant.getId())) {
            throw new BadRequestException("This email has been registered under this merchant");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(AccountStatus.ACTIVE);
        user.setRole(UserRole.CUSTOMER);
        user.setMerchant(merchant);
        userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent();
        event.setEmail(user.getEmail());
        event.setRole(user.getRole().toString());
        eventPublisher.publishEvent(event);

        return packageResponseDTO(user, merchant);
    }
    public UserDTO resetPassword(UserDTO userDTO){

        User existingUser = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

//      1. compare with previous
        if (passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password");
        }

        // 2. Encode and save the new password
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserDTO dto = new UserDTO();
        dto.setStatus(existingUser.getStatus().getDescription());
        dto.setEmail(existingUser.getEmail());
        dto.setMerchantName(existingUser.getMerchant().getName());
        dto.setMerchantId(existingUser.getMerchant().getId());
        return dto;
    }

    @Override
    public UserDTO packageResponseDTO(User user,Merchant merchant) {
        UserDTO responseDTO =new UserDTO();

        responseDTO.setEmail(user.getEmail());
        responseDTO.setMerchantName(merchant.getName());
        responseDTO.setStatus(user.getStatus().getDescription());

        return responseDTO;
    }

    public InviteToken validateInviteToken(String token) {
        InviteToken inviteToken = inviteTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Token not found"));

        if (LocalDateTime.now().isAfter(inviteToken.getExpiryDate())) {
            throw new BadRequestException("Token is expired");
        }

        return inviteToken;
    }

    @Override
    public void resetFailedAttempts(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setFailedAttempts(0);
            user.setAccountLocked(false);
            userRepository.save(user);
        });
    }

}
