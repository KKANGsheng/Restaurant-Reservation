package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    boolean existsByEmail(String email);

    Optional<Merchant> findByEmail(String email);

    boolean existsBySlug(String slug);

    Optional<Merchant> findBySlug(String slug);
}
