package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.dto.RestaurantDTO;
import com.smart.restaurantAppointment.dto.request.RestaurantReq;
import com.smart.restaurantAppointment.dto.response.ApiResponse;
import com.smart.restaurantAppointment.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping("/restaurants")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantReq req) {
        Restaurant restaurant = restaurantService.createRestaurant(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @GetMapping("getCustomerRestaurants")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<?> getCustomerRestaurants() {
        return  ResponseEntity.ok(restaurantService.getCustomerRestaurants());
    }

    //  getMerchantRestaurants
    @GetMapping("getAllRestaurants")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<ApiResponse<Restaurant>> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantReq req) {
        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return ResponseEntity.ok(ApiResponse.success("Restaurant updated successfully", restaurant));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Void> deleteRestaurant (@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

}
