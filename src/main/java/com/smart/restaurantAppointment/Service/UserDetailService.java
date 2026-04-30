package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.MerchantUserDetails;
import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

//  Using two one for merchant one for user
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return new MyUserDetails(user.get());
        }

        Optional<Merchant> merchant = merchantRepository.findByEmail(username);
        if (merchant.isPresent()) {
            return new MerchantUserDetails(merchant.get());
        }

        throw new UsernameNotFoundException("User Not Found");
    }
}
