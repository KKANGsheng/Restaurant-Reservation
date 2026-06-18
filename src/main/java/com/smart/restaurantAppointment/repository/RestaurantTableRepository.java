package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.entity.Reservation;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.RestaurantTable;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable,Long> {

    @EntityGraph(attributePaths = {"restaurant"})
    List<RestaurantTable> findByRestaurant(Restaurant restaurant);

//  10:00 -12:00
//  existing start 10 < (end)
//  end

    @Query("""
            SELECT t FROM RestaurantTable t  
            WHERE t.restaurant = :restaurant  
            AND   t.capacity >=  :capacity  
            AND   t.id NOT IN 
                (SELECT res.assignedTable.id FROM Reservation res
                 WHERE res.assignedTable.restaurant = :restaurant
                 AND   res.reservationDateTime < :end
                 AND   res.endDateTime > :start
                 AND   res.status <> com.smart.restaurantAppointment.Enumerator.ReservationStatus.CANCELED)
             ORDER BY t.capacity ASC
           """)
    List<RestaurantTable> findFreeTablesFittingSize(
            @Param("restaurant") Restaurant restaurant,
            @Param("capacity") int capacity,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);
}
