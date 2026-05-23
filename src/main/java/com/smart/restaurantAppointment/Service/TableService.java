package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.dto.Request.TableReq;
import com.smart.restaurantAppointment.dto.Request.UpdateTableReq;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.RestaurantTable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableService {

    public RestaurantTable createTable(TableReq req);

    public List<RestaurantTable> getRestaurantTables(Long restaurantId);

    public RestaurantTable updateTable(Long tableId, UpdateTableReq req);

    public void deleteTable(Long tableId);

    public Optional<RestaurantTable> findBestFitFreeTable (Restaurant restaurant, int capacity, LocalDateTime start, LocalDateTime end);
}
