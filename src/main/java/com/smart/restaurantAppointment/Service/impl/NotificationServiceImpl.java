package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Service.NotificationService;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.config.AppConfig;
import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.PasswordResetRequestedEvent;
import com.smart.restaurantAppointment.dto.UserCreatedEvent;
import com.smart.restaurantAppointment.entity.Merchant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        message.setSubject("Forgot Password Reset");
        message.setText(appConfig.baseUrl  + "/forgot-password" + "?token=" + event.getToken());
        mailSender.send(message);
    }
}
