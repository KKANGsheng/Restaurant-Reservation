package com.smart.restaurantAppointment.Service.impl;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.EventPurpose;
import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.*;
import com.smart.restaurantAppointment.dto.Request.ChangePasswordReq;
import com.smart.restaurantAppointment.dto.Request.CreatedUserByMerchantReq;
import com.smart.restaurantAppointment.dto.Request.ResetPasswordConfirmReq;
import com.smart.restaurantAppointment.entity.InviteToken;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.PasswordResetToken;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.InviteTokenRepository;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.PasswordResetTokenRepository;
import com.smart.restaurantAppointment.repository.UserRepository;
import com.smart.restaurantAppointment.util.SecurityUtils;
import com.smart.restaurantAppointment.util.ValidationUtils;
import io.micrometer.common.util.StringUtils;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MerchantRepository merchantRepository;
    private final InviteTokenRepository inviteTokenRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final SecurityUtils securityUtils;


    @Override
    public UserDTO register(CreatedUserByMerchantReq registerReq) {
        return createUserByMerchant(registerReq.getEmail(), registerReq.getPhoneNumber(), registerReq.getName());
    }

    public UserDTO registerViaInvite(String email, String password, String token) {
        InviteToken inviteToken = validateInviteToken(token);
        Merchant merchant = inviteToken.getMerchant();

        return createUserWithPassword(email, password, merchant);
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

    private UserDTO createUserWithPassword(String email, String password, Merchant merchant) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new BadRequestException("Email and password are required");
        }

        if (userRepository.existsByEmailAndMerchantId(email, merchant.getId())) {
            throw new BadRequestException("This email has been registered under this merchant");
        }

        User user = new User();
        user.setEmail(email);
        user.setStatus(AccountStatus.ACTIVE);
        user.setRole(UserRole.CUSTOMER);
        user.setMerchant(merchant);
        user.setAccountLocked(Boolean.TRUE);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent();
        event.setEmail(user.getEmail());
        event.setRole(user.getRole().toString());
        eventPublisher.publishEvent(event);

        return packageResponseDTO(user, merchant);
    }

    public void resetPassword(ChangePasswordReq request) {
       User existingUser = securityUtils.getCurrentUser();

//      1. compare old password with user enter password

        if (!passwordEncoder.matches(request.oldPassword(),existingUser.getPassword())) {
            throw new BadRequestException("old password is incorrect");
        }

//      2. new password cannot be same as old password
        if (passwordEncoder.matches(request.newPassword(), existingUser.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password");
        }

        // 2. Encode and save the new password
        existingUser.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(existingUser);
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

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new BadRequestException("user is not exist"));
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        passwordResetToken.setUser(user);
        passwordResetToken.setEmail(user.getEmail());
        passwordResetTokenRepository.save(passwordResetToken);

        PasswordResetRequestedEvent event =  new PasswordResetRequestedEvent();
        event.setEmail(user.getEmail());
        event.setExpiresAt(passwordResetToken.getExpiryDate());
        event.setToken(passwordResetToken.getToken());
        event.setPurpose(EventPurpose.RESET_PASSWORD.name());
        eventPublisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void resetPasswordConfirm(ResetPasswordConfirmReq resetPasswordConfirmReq) {
     PasswordResetToken  passwordResetToken = passwordResetTokenRepository.findByToken(resetPasswordConfirmReq.token())
                                                                        .orElseThrow(()-> new BadRequestException("password Reset Token did not exist"));
     if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
         throw new BadRequestException("invalid or expired token");
     }
     User user  = passwordResetToken.getUser();
     user.setPassword(passwordEncoder.encode(resetPasswordConfirmReq.newPassword()));
     user.setAccountLocked(false);
     user.setFailedAttempts(0);
     userRepository.save(user);
     passwordResetTokenRepository.deleteAllByUser(user);
    }

    @Transactional
    public UserDTO createUserByMerchant (String email ,String phoneNumber, String name) {
        Merchant merchant = securityUtils.getCurrentMerchant();
        if (userRepository.existsByEmailAndMerchantId(email, merchant.getId())) {
            throw new BadRequestException("This email has been registered");
        }
        boolean isValidPhoneNumber = ValidationUtils.isValidPhoneNumber(phoneNumber);
        if (!isValidPhoneNumber) {
            throw new BadRequestException("Phone Number is invalid please retry again");
        }

        User user =  new User();
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAccountLocked(Boolean.TRUE);
        user.setStatus(AccountStatus.ACTIVE);
        user.setRole(UserRole.CUSTOMER);
        user.setMerchant(merchant);
        user.setName(name);
        userRepository.save(user);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        passwordResetToken.setUser(user);
        passwordResetToken.setEmail(email);
        passwordResetTokenRepository.save(passwordResetToken);

        PasswordResetRequestedEvent event = new PasswordResetRequestedEvent();
        event.setName(user.getName());
        event.setEmail(user.getEmail());
        event.setToken(passwordResetToken.getToken());
        event.setPurpose(EventPurpose.NEW_USER_INVITE.name());
        eventPublisher.publishEvent(event);
        return packageResponseDTO(user, merchant);
    }

}
