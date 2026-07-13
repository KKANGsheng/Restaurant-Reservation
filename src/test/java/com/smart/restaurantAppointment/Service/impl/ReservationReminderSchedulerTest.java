package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Service.NotificationService;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import com.smart.restaurantAppointment.scheduler.ReservationReminderScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationReminderSchedulerTest {
        @Mock
        private ReservationRepository reservationRepository;

        @Mock
        private NotificationService notificationService;

//        @InjectMocks
//        private ReservationReminderScheduler scheduler;
//
//        @Test
//        @DisplayName("sends reminder for booking due in 1 hour and markes reminderSend=true")
//        void sendReminderForBookingDueInOneHour() {
//                Reservation r = new Reservation();
//                r.setReservationDateTime(LocalDateTime.now().plusMinutes(60));
//                r.setStatus(ReservationStatus.CONFIRMED);
//                r.setReminder(false);
//
//                when(reservationRepository.findRemindersDue(any(),any()))
//                        .thenReturn(List.of(r));
//
////                scheduler
//                verify(notificationService).sendBookingRemindersDue(r);
//        }
}
