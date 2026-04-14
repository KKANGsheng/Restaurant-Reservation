package com.smart.restaurantAppointment.Service.impl;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private RedisTemplate<String,String> redisTemplate;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    @Test
    void createReservation_pastDateTime_throwsException() {
        User user = new User();
        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setReservationDateTime(LocalDateTime.now().minusDays(1));

        Restaurant restaurant = new Restaurant();
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        restaurant.setMerchant(merchant);
        user.setMerchant(merchant);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            reservationService.validateReservation(restaurant, user, dto);
        });
    }



}
