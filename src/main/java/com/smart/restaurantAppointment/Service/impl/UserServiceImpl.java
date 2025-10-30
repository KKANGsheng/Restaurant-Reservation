package com.smart.restaurantAppointment.Service.impl;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.UserService;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import com.smart.restaurantAppointment.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MerchantRepository merchantRepository;

    public UserDTO register(UserDTO register){
        System.out.println("enter function bro");
        User user=new User();

        if(StringUtils.isBlank(register.getEmail()) || StringUtils.isBlank(register.getPassword())){
            throw new BadRequestException("Email and password are required");
        }

        Boolean existUser=userRepository.existsByEmailAndMerchantId(register.getEmail(), register.getMerchantId());

        if(existUser){
            throw new BadRequestException("this email has been registered under this merchant");
        }

        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setStatus(AccountStatus.PENDING_ACTIVATION);
        user.setRole(UserRole.CUSTOMER);

//        Merchant merchant = new Merchant();
//        merchant.setId(register.getMerchantId());

        Merchant merchant = merchantRepository.findById(register.getMerchantId())
                .orElseThrow(() -> new BadRequestException("Merchant not found"));
        user.setMerchant(merchant);

        userRepository.save(user);

        return packageResponseDTO(user,merchant);
    }
    public User resetPassword(UserDTO userDTO, Long id){

//     Get user
       Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userDTO.getEmail()));

       User existingUser= user.get();

//      1. compare with previous
        if (passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password");
        }

        // 2. Encode and save the new password
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(existingUser);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @Override
    public UserDTO packageResponseDTO(User user,Merchant merchant) {
        UserDTO responseDTO =new UserDTO();

        responseDTO.setEmail(user.getEmail());
        responseDTO.setMerchantName(merchant.getName());
        responseDTO.setStatus(user.getStatus().getDescription());

        return responseDTO;
    }

}
