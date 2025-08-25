package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Enumerator.UserRole;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.dto.MerchantRegisterDTO;
import com.smart.restaurantAppointment.dto.UserRegisterDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegisterDTO register){
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

        Merchant merchant = new Merchant();
        merchant.setId(register.getMerchantId());
        user.setMerchant(merchant);

        return userRepository.save(user);

    }
}
