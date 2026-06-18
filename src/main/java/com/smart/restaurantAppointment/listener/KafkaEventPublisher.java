package com.smart.restaurantAppointment.listener;

import com.smart.restaurantAppointment.dto.event.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.event.MerchantCreatedEvent;
import com.smart.restaurantAppointment.dto.event.PasswordResetRequestedEvent;
import com.smart.restaurantAppointment.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
// A listener that will listen to spring boot applicationEventPublisher
public class KafkaEventPublisher {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMerchantCreated(MerchantCreatedEvent event) {
        log.info("Merchant Created, sending merchantCreated topics to kafka");
        kafkaTemplate.send("merchant-created",event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreated(UserCreatedEvent event) {
        log.info("user Created, sending userCreated topics to kafka");
        kafkaTemplate.send("user-created",event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingCreated(BookingCreatedEvent event){
        log.info("booking created, sending BookingCreated topics to kafka");
        kafkaTemplate.send("booking-created",event);
    }

    @TransactionalEventListener(phase =TransactionPhase.AFTER_COMMIT)
    public void onPasswordResetRequest (PasswordResetRequestedEvent event) {
        kafkaTemplate.send("reset-password", event);
    }

}
