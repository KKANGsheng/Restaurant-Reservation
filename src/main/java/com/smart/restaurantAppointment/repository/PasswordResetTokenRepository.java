package com.smart.restaurantAppointment.repository;

import com.smart.restaurantAppointment.entity.PasswordResetToken;
import com.smart.restaurantAppointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {

    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByTokenAndEmail(String token,String email);
    void deleteAllByUser(User user);
}
