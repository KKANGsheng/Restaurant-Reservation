package com.smart.restaurantAppointment.Service.impl;
import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RedisTemplate<String,String> redisTemplate;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User user;
    private Merchant merchant;
    private Restaurant restaurant;
    private ReservationRequestDTO dto;
    private Merchant merchant2;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();
        merchant.setId(12L);
        user = new User();
        user.setId(10L);
        user.setEmail("obx@gmail.com");
        user.setMerchant(merchant);

        restaurant= new Restaurant();
        restaurant.setId(10L);
        restaurant.setMerchant(merchant);

        dto = new ReservationRequestDTO();
        dto.setRestaurantId(10L);
        dto.setReservationDateTime(LocalDateTime.now().plusDays(10));
        dto.setSize(3);

        reservation = new Reservation();
        reservation.setId(50L);
        reservation.setRestaurant(restaurant);
        reservation.setCustomer(user);
        reservation.setStatus(ReservationStatus.PENDING_CONFIRMATION);
        reservation.setReservationDateTime(LocalDateTime.now().plusDays(1));
    }

    @DisplayName("create Reservation - past date")
    @Test
    void createReservationPastDateTimeThrowsException() {
        dto.setReservationDateTime(LocalDateTime.now().minusDays(1));
        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            reservationService.validateReservation(restaurant, user, dto);
        },"Cannot book before todays Date Unit Testing");
    }

    @Test
    @DisplayName("create Reservation throws error when restaurant belongs to different merchant")
    void createReservationCrossTenantBooking() {
        Merchant merchant = new Merchant();
        user.setMerchant(merchant);
        assertThrows(BadRequestException.class, () ->{
            reservationService.validateReservation(restaurant, user, dto);
        },"booking must be made with the same tenant");
    }

    @Test
    @DisplayName("Create Reservation will throw Exception if the reservation is null")
    void createReservationUserNullMerchantThrows() {
       user.setMerchant(null);
       assertThrows(BadRequestException.class, ()->{
           reservationService.validateReservation(restaurant, user, dto);
       },"merchant null must throw error");
   }

    @Test
    @DisplayName("Create Reservation happy Flow")
    void createReservationValidate() {
        assertDoesNotThrow(() -> reservationService.validateReservation(restaurant, user, dto));
    }


}
