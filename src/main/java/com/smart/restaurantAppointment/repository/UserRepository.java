package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmailAndMerchantId(String email,Long merchantId);

    Optional<User> findByEmail(String email);
}
