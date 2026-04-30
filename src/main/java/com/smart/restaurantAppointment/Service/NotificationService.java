package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.UserCreatedEvent;
import com.smart.restaurantAppointment.entity.Merchant;
public interface NotificationService {
    void sendUserCreatedNotification(UserCreatedEvent userCreatedEvent);
    void sendBookingEventNotification(BookingCreatedEvent bookingCreatedEvent);
    void SendMerchantCreatedNotification(MerchantCreatedEvent merchant);

}
