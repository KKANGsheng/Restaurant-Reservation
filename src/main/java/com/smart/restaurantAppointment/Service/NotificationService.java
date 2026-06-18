package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.event.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.event.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.event.PasswordResetRequestedEvent;
import com.smart.restaurantAppointment.dto.event.UserCreatedEvent;

import java.time.LocalDateTime;

public interface NotificationService {
    void sendUserCreatedNotification(UserCreatedEvent userCreatedEvent);
    void sendBookingEventNotification(BookingCreatedEvent bookingCreatedEvent);
    void sendMerchantCreatedNotification(MerchantCreatedEvent merchant);
    void sendPasswordResetEventNotification (PasswordResetRequestedEvent passwordResetRequest);
    void sendReservationRemindersDue(String email, String customerName, LocalDateTime reservationDate, String restaurantName);
}
