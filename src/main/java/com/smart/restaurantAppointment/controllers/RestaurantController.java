package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.ReservationService;
import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.dto.Request.RestaurantReq;
import com.smart.restaurantAppointment.dto.Request.TableReq;
import com.smart.restaurantAppointment.dto.response.ApiResponse;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.RestaurantTable;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("getCustomerRestaurants")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<?> getCustomerRestaurants() {
        return  ResponseEntity.ok(restaurantService.getCustomerRestaurants());
    }

    //  getMerchantRestaurants
    @GetMapping("getAllRestaurants")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @PostMapping("/restaurants")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantReq req) {
        Restaurant restaurant = restaurantService.createRestaurant(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @PutMapping("/restaurants/{id}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<ApiResponse<Restaurant>> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantReq req) {
        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return ResponseEntity.ok(ApiResponse.success("Restaurant updated successfully", restaurant));
    }


    @DeleteMapping("restaurants/{id}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Void> deleteRestaurant (@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

}
