package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.*;
import com.smart.restaurantAppointment.entity.Merchant;

import java.time.LocalDateTime;

public interface NotificationService {
    void sendUserCreatedNotification(UserCreatedEvent userCreatedEvent);
    void sendBookingEventNotification(BookingCreatedEvent bookingCreatedEvent);
    void sendMerchantCreatedNotification(MerchantCreatedEvent merchant);
    void sendPasswordResetEventNotification (PasswordResetRequestedEvent passwordResetRequest);
    void sendReservationRemindersDue(String email, String customerName, LocalDateTime reservationDate, String restaurantName);
}
