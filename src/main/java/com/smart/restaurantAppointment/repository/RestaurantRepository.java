package com.smart.restaurantAppointment.repository;


import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    List<Restaurant> findByMerchant(Merchant merchant);

}
