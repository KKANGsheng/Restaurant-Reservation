package com.smart.restaurantAppointment.consumer;

import com.smart.restaurantAppointment.Service.NotificationService;
import com.smart.restaurantAppointment.dto.event.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.event.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.event.PasswordResetRequestedEvent;
import com.smart.restaurantAppointment.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "booking-created", groupId = "notification-group")
    public void handleBookingCreated(BookingCreatedEvent event) {
        notificationService.sendBookingEventNotification(event);
    }

    @KafkaListener(topics = "user-created", groupId = "notification-group")
    public void handleUserCreated(UserCreatedEvent event) {
        notificationService.sendUserCreatedNotification(event);
    }

    @KafkaListener(topics =  "merchant-created", groupId = "notification-group")
    public void handleMerchantCreated(MerchantCreatedEvent event) {
        notificationService.sendMerchantCreatedNotification(event);
    }

    @KafkaListener(topics =  "reset-password", groupId = "notification-group")
    public void handlePasswordResetEventCreated(PasswordResetRequestedEvent event) {
        notificationService.sendPasswordResetEventNotification(event);
    }

}
