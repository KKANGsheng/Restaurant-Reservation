package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.AccountStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.dto.Request.RestaurantReq;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.MerchantUserDetails;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.User;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.util.DateTimeUtils;
import com.smart.restaurantAppointment.util.RedisKey;
import com.smart.restaurantAppointment.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getAllRestaurants() {
        MerchantUserDetails details = (MerchantUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = details.getMerchant();
        List<Restaurant> restaurants = restaurantRepository.findByMerchant(merchant);

        if (restaurants.isEmpty()) {
            throw new BadRequestException("Merchant is not exist");
        }

        return restaurants;
    }

    @Override
    public Restaurant createRestaurant(RestaurantReq dto) {
        Merchant merchant = SecurityUtils.getCurrentMerchant();
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
        User user = SecurityUtils.getCurrentUser();
        Merchant merchant = user.getMerchant();
        if (merchant == null) {
            throw new BadRequestException("Customer must linked to a merchant");
        }
        return restaurantRepository.findByMerchant(merchant);
    }

    @Override
    public void validateRestaurantOwnership(Restaurant restaurant) {
        Merchant merchant = SecurityUtils.getCurrentMerchant();
        if (!restaurant.getMerchant().getId().equals(merchant.getId())) {
            throw new BadRequestException("You can only manage your own restaurant");
        }
    }
}
