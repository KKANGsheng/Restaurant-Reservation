package com.smart.restaurantAppointment.repository;


import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.dto.response.ReservationResponseDTO;
import com.smart.restaurantAppointment.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation,Long> {
    Page<Reservation> findByCustomer(User customer, Pageable pageable);
    Page<Reservation> findByRestaurantIn(List<Restaurant> restaurants, Pageable pageable);
    @Query("SELECT r from Reservation r " +
            "WHERE r.assignedTable = :table " +
            "AND r.reservationDateTime <:end " +
            "AND r.endDateTime >:start " +
            "AND r.status <> :excludedStatus ")
    List<Reservation> findOverlappingRestaurant (@Param("table") RestaurantTable table, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("excludedStatus")ReservationStatus excludedStatus);

    @Query("""
            SELECT r from Reservation r
            WHERE r.restaurant.merchant.id =:merchantId 
            AND (:restaurantId IS NULL OR r.restaurant.id =:restaurantId)
            AND (:reservationStatus IS NULL OR r.status =:reservationStatus)
            AND (:fromDate IS NULL OR r.reservationDateTime >=:fromDate)
            AND (:toDate IS NULL OR r.reservationDateTime <=:toDate)
            AND (:customerName IS NULL OR LOWER (r.customer.name)LIKE LOWER(CONCAT('%',:customerName,'%')))
            AND (:email IS NULL OR r.customer.email =:email)
            """)
    Page<Reservation> searchReservations    (@Param("merchantId")Long merchantId,
                                             @Param("restaurantId")Long restaurantId,
                                            @Param("reservationStatus")ReservationStatus reservationStatus,
                                            @Param("fromDate")LocalDateTime fromDate,
                                            @Param("toDate")LocalDateTime toDate,
                                            @Param("customerName")String customerName,
                                            @Param("email")String email, Pageable pageable);

    @Query("""
           SELECT r from Reservation r
           WHERE r.status =:reservationStatus
           AND r.reservationDateTime >= :from
           AND r.reservationDateTime  <=:to
           AND r.reminder = false
           """)
    List<Reservation> findRemindersDue(@Param("from")LocalDateTime from,@Param("to") LocalDateTime to,@Param("reservationStatus") ReservationStatus reservationStatus);
}
