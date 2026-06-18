package com.smart.restaurantAppointment.Service;

import com.smart.restaurantAppointment.dto.request.ChangePasswordReq;
import com.smart.restaurantAppointment.dto.request.CreatedUserByMerchantReq;
import com.smart.restaurantAppointment.dto.request.ResetPasswordConfirmReq;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.InviteToken;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.User;

public interface UserService {

    UserDTO register(CreatedUserByMerchantReq registerReq);
    void resetPassword(ChangePasswordReq changePasswordReq);

    UserDTO packageResponseDTO(User user, Merchant merchant);

    InviteToken validateInviteToken(String Token);

    UserDTO registerViaInvite(String email, String password, String token);

    void incrementFailedAttempts(String email);

    void resetFailedAttempts(String email);

    void forgotPassword(String email);

    void resetPasswordConfirm(ResetPasswordConfirmReq resetPasswordConfirmReq);

}
