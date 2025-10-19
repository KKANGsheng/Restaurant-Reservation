package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.UserCreatedEvent;
import com.smart.restaurantAppointment.entity.Merchant;
import org.springframework.kafka.annotation.KafkaListener;

public class NotificationServiceImpl implements  NotificationService{

    @Override
    @KafkaListener(topics = "user-created", groupId = "notification-group")
    public void sendUserCreatedNotification(UserCreatedEvent userCreatedEvent) {
        System.out.println("sending userCreationEmail");
    }

    @Override
    @KafkaListener(topics = "booking-created", groupId = "notification-group")
    public void sendBookingEventNotification(BookingCreatedEvent bookingCreatedEvent) {
        System.out.println("send Booking Event Notification");
    }

    @Override
    @KafkaListener(topics = "merchant-created", groupId = "notification-group")
    public void SendMerchantCreatedNotification(Merchant merchant) {
        System.out.println("Send Merchant Created Notification");
    }
}
