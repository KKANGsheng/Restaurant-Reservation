package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.entity.MyUserDetails;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByEmail(username);

        if(user ==null){
            throw new UsernameNotFoundException("User Not Found");
        }

        return new MyUserDetails(user);
    }
}
