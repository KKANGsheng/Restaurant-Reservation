package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Exception.ConflictException;
import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.Service.TableService;
import com.smart.restaurantAppointment.dto.BookingCreatedEvent;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.dto.response.PageResponse;
import com.smart.restaurantAppointment.dto.response.ReservationResponseDTO;
import com.smart.restaurantAppointment.entity.*;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.util.DateTimeUtils;
import com.smart.restaurantAppointment.util.RedisKey;
import com.smart.restaurantAppointment.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private record BookingKeys(String lockKey, String bookedKey) {}
    private final  RedisTemplate<String, String> redisTemplate;
    private final  ApplicationEventPublisher eventPublisher;
    private final TableService tableService;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto, User user) {
        Reservation reservation =new Reservation();
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElseThrow(()-> new BadRequestException("Restaurant did not exist"));
        validateReservation(restaurant, user, dto);
        BookingKeys keys = validateDuplicateRequest(user,restaurant,dto);
        reservation.setCustomer(user);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationDateTime(dto.getReservationDateTime());
        reservation.setSize(dto.getSize());
        reservation.setRestaurant(restaurant);
        reservation.setReminder(Boolean.FALSE);

        LocalDateTime start = dto.getReservationDateTime();
        LocalDateTime end = dto.getReservationDateTime().plusMinutes(restaurant.getDefaultBookingMinutes());

        RestaurantTable table= tableService.findBestFitFreeTable(restaurant,dto.getSize(),start, end).orElseThrow(()-> new ConflictException("No tables available at this time. Please pick another time slot"));

        reservation.setAssignedTable(table);
        reservation.setEndDateTime(end);
        Reservation saved = reservationRepository.save(reservation);

        redisTemplate.delete(keys.lockKey());
        redisTemplate.opsForValue().set(keys.bookedKey(), "1", RedisKey.BOOKING_BOOKED_TTL);
//      publish event
        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setRefId(reservation.getId());
        event.setEmail(user.getEmail());
        event.setReservationDateTime(reservation.getReservationDateTime());
        event.setRestaurantName(restaurant.getName());
        event.setSize(reservation.getSize());
        eventPublisher.publishEvent(event);
        return ReservationResponseDTO.from(saved);
    }

    @Override
    public void validateReservation(Restaurant restaurant,User user, ReservationRequestDTO dto) {
        LocalDateTime bookingTime = dto.getReservationDateTime();
        if (user.getMerchant() == null || !restaurant.getMerchant().getId().equals(user.getMerchant().getId())) {
            throw new BadRequestException("You are not authorized to book this restaurant");
        }

        if (bookingTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("cannot book the time that in the past");
        }

        if (dto.getSize() <= 0) {
            throw new BadRequestException("capacity cannot smaller than zero");
        }

        if (bookingTime.toLocalTime().isBefore(restaurant.getOpeningTime())) {
            throw  new BadRequestException("Booking time is before opening Time");
        }

        if (!bookingTime.toLocalTime().isBefore(restaurant.getClosingTime())) {
            throw new BadRequestException("Booking time is outside opening hours");
        }

        long minutesFromOpening = Duration.between(restaurant.getOpeningTime(), bookingTime.toLocalTime()).toMinutes();

        if (minutesFromOpening % restaurant.getSlotIntervalMinutes() !=0) {
            throw new BadRequestException("Booking time must align to the restaurant's slot interval");
        }

    }

    @Override
    public Page<ReservationResponseDTO>  getCustomerReservations(Pageable pageable) {
        User user = securityUtils.getCurrentUser();
        return reservationRepository.findByCustomer(user, pageable)
                .map(ReservationResponseDTO::from);
    }

    @Override
    public PageResponse<ReservationResponseDTO> getMerchantReservations(Pageable pageable, LocalDate fromDate, LocalDate toDate, ReservationStatus reservationStatus, String customerName, String email, Long restaurantId) {
        Merchant merchant = securityUtils.getCurrentMerchant();
        LocalDateTime fromDateTime = fromDate != null ?  fromDate.atStartOfDay():null;
        LocalDateTime endDateTime  = toDate != null ? toDate.atTime(23,59,59):null;

        Page<Reservation> page= reservationRepository.searchReservations(merchant.getId(),restaurantId,reservationStatus, fromDateTime, endDateTime,customerName,email,pageable);
        Page<ReservationResponseDTO> dtoPage = page.map(ReservationResponseDTO::from);
        return PageResponse.from(dtoPage);
    }

    @Override
    public ReservationResponseDTO cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new BadRequestException("Reservation not found"));
        Merchant merchant = securityUtils.getCurrentMerchant();

        if (!reservation.getRestaurant().getMerchant().getId().equals(merchant.getId())) {
            throw new BadRequestException("You can only cancel reservations for your own restaurant");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new BadRequestException("Reservation is already cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELED);
        String dateHour = DateTimeUtils.formatDateHour(reservation.getReservationDateTime());
        String bookedKey = RedisKey.bookingBooked(reservation.getCustomer().getId(), reservation.getRestaurant().getId(), dateHour);
        redisTemplate.delete(bookedKey);
        return ReservationResponseDTO.from(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseDTO confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new BadRequestException("Reservation not found"));
        Merchant merchant = securityUtils.getCurrentMerchant();

        if (!reservation.getRestaurant().getMerchant().getId().equals(merchant.getId())) {
            throw new BadRequestException("You can only confirm reservations for your own restaurant");
        }

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Reservation is already confirmed");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new BadRequestException("Reservation status cancel cannot be changed");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        return ReservationResponseDTO.from(reservationRepository.save(reservation));
    }

    @Override
    public PageResponse<ReservationResponseDTO> getReservationHistory(Pageable pageable) {
       User user = securityUtils.getCurrentUser();
       Pageable sorted = PageRequest.of(
                         pageable.getPageNumber(),
                         pageable.getPageSize(),
                         Sort.by("reservationDateTime").descending()
                         );
       Page<Reservation> page = reservationRepository.findByCustomer(user, sorted);
       return  PageResponse.from(page.map(ReservationResponseDTO::from));
    }

    private BookingKeys validateDuplicateRequest (User user, Restaurant restaurant, ReservationRequestDTO dto) {
        String dateHour = DateTimeUtils.formatDateHour(dto.getReservationDateTime());
        String lockKey = RedisKey.bookingLock(user.getId(), restaurant.getId(), dateHour);
        String bookedKey = RedisKey.bookingBooked(user.getId(), restaurant.getId(), dateHour);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(bookedKey))) {
            throw new BadRequestException("You already have a reservation for this time slot");
        }
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", RedisKey.BOOKING_LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(locked)) {
            throw new BadRequestException("Please wait a moment and try again");
        }
        return new BookingKeys(lockKey, bookedKey);
    }

}
