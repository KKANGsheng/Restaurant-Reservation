package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.dto.RestaurantDTO;
import com.smart.restaurantAppointment.dto.request.RestaurantReq;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.MerchantUserDetails;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final SecurityUtils securityUtils;

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        MerchantUserDetails details = (MerchantUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = details.getMerchant();
        List<RestaurantDTO> restaurants = restaurantRepository
                                        .findByMerchant(merchant)
                                        .stream()
                                        .map(RestaurantDTO::from).toList();
        if (restaurants.isEmpty()) {
            throw new BadRequestException("Merchant is not exist");
        }
        return restaurants;
    }

    @Override
    public Restaurant createRestaurant(RestaurantReq dto) {
        Merchant merchant = securityUtils.getCurrentMerchant();
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setContactNumber(dto.getContactNumber());
        restaurant.setMerchant(merchant);
        restaurant.setStatus(AccountStatus.ACTIVE);
        restaurant.setOpeningTime(dto.getOpeningTime());
        restaurant.setClosingTime(dto.getClosingTime());
        restaurant.setDefaultBookingMinutes(90);
        restaurant.setSlotIntervalMinutes(dto.getSlotIntervalMinutes());
        restaurant.setRestaurantCategory(dto.getRestaurantCategory());
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new BadRequestException("restaurant did not exist"));
        validateRestaurantOwnership(restaurant);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long id, RestaurantReq dto) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new BadRequestException("restaurant did not exist"));
        validateRestaurantOwnership(restaurant);
        restaurant.setAddress(dto.getAddress());
        restaurant.setName(dto.getName());
        restaurant.setContactNumber(dto.getContactNumber());
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public List<Restaurant> getCustomerRestaurants() {
        User user = securityUtils.getCurrentUser();
        Merchant merchant = user.getMerchant();
        if (merchant == null) {
            throw new BadRequestException("Customer must linked to a merchant");
        }
        return restaurantRepository.findByMerchant(merchant);
    }

    @Override
    public void validateRestaurantOwnership(Restaurant restaurant) {
        Merchant merchant = securityUtils.getCurrentMerchant();
        if (!restaurant.getMerchant().getId().equals(merchant.getId())) {
            throw new BadRequestException("You can only manage your own restaurant");
        }
    }
}
