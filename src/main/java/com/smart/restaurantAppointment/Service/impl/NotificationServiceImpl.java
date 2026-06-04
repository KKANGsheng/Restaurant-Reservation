package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.EventPurpose;
import com.smart.restaurantAppointment.Service.NotificationService;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.config.AppConfig;
import com.smart.restaurantAppointment.dto.*;
import com.smart.restaurantAppointment.entity.Merchant;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final AppConfig appConfig;

    @Override
    public void sendUserCreatedNotification(UserCreatedEvent userCreatedEvent) {
        log.info("send user created Notification");
        SimpleMailMessage message =  new SimpleMailMessage();
        message.setTo(userCreatedEvent.getEmail());
        message.setSubject("User Has been created");
        message.setText("User with the email " + userCreatedEvent.getEmail() +
                       " has been created");
        mailSender.send(message);
    }

    @Override
    public void sendBookingEventNotification(BookingCreatedEvent bookingCreatedEvent) {
        log.info("send Booking Event Notification");
        SimpleMailMessage message =  new SimpleMailMessage();
        message.setTo(bookingCreatedEvent.getEmail());
        message.setSubject("Reservation Created");
        message.setText("Your Reservation at " + bookingCreatedEvent.getRestaurantName()
                       + " on " + bookingCreatedEvent.getRestaurantName()
                       + " has received ");
        mailSender.send(message);
//        log.info("Received booking event: {}", bookingCreatedEvent);
//        throw new RuntimeException("forcing DLT test");
    }

    @Override
    public void sendMerchantCreatedNotification(MerchantCreatedEvent event) {
        log.info("send merchant created Notification");
        SimpleMailMessage message =  new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Merchant Has been created");
        message.setText("User with the email " + event.getEmail() +
                " has been created");
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEventNotification(PasswordResetRequestedEvent event) {
        log.info("send forgot password Event notification");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        String link = appConfig.baseUrl  + "/forgot-password?token=" +event.getToken();
        if ((EventPurpose.RESET_PASSWORD.name().equals(event.getPurpose()))) {
            message.setSubject("Reset password");
            message.setText(
                    "We received a request to reset your password.\n\n" +
                            "Click the link to reset:\n" + link + "\n\n" +
                            "If you didn't request this, ignore this email.\n\n" +
                            "Link expires at " + event.getExpiresAt() + "."
            );
        } else if ((EventPurpose.NEW_USER_INVITE.name().equals(event.getPurpose()))) {
            message.setSubject("Welcome " + event.getName() + " Please set your password");
            message.setText(
                    "Hi " + event.getName() + ",\n\n" +
                            "An account has been created for you. Please click the link below to set your password:\n\n" +
                            link + "\n\n" +
                            "This link expires at " + event.getExpiresAt() + ".\n\n" +
                            "Thanks!"
            );
        }
        mailSender.send(message);
    }

    @Override
}
