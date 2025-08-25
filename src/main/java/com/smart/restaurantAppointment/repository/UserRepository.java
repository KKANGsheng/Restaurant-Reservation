package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmailAndMerchantId(String email,Long merchantId);

    User findByEmail(String email);
}
