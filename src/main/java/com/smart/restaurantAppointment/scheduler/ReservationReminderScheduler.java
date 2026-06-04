package com.smart.restaurantAppointment.scheduler;

import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Service.NotificationService;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name= "app.scheduler.reminder.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ReservationReminderScheduler {
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 10000)
    public void sendReminders() {
        LocalDateTime from = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime to  = from.plusDays(1).minusNanos(1);
        List<Reservation> reservationList = reservationRepository.findRemindersDue(from,to, ReservationStatus.CONFIRMED);

        for (Reservation reservation:reservationList) {
            reservation.setReminder(true);
            notificationService.sendReservationRemindersDue(reservation.getCustomer().getEmail(), reservation.getCustomer().getName(), reservation.getReservationDateTime(), reservation.getRestaurant().getName());
        }
    }

}
