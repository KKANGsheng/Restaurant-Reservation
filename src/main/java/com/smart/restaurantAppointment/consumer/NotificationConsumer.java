package com.smart.restaurantAppointment.consumer;


import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final JavaMailSender mailSender;

    @KafkaListener(topics = "booking-created", groupId = "notification-group")
    public void handleBookingCreated (BookingCreatedEvent event) {
        log.info("New booking received: reservation Id={}, customer={}, restaurant={}"
                ,event.getRefId(),event.getCustomerName(),event.getRestaurantName());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Reservation Confirmed");
        message.setText("Your Reservation at " + event.getRestaurantName()
                       + " on " + event.getReservationDateTime()
                       + " has received ");
        mailSender.send(message);
    }
}
