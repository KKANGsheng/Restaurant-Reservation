package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.dto.request.TableReq;
import com.smart.restaurantAppointment.dto.request.UpdateTableReq;
import com.smart.restaurantAppointment.dto.RestaurantTableDTO;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.RestaurantTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableService {

    public RestaurantTable createTable(TableReq req);

    public List<RestaurantTableDTO> getRestaurantTables(Long restaurantId);

    public RestaurantTable updateTable(Long tableId, UpdateTableReq req);

    public void deleteTable(Long tableId);

    public Optional<RestaurantTable> findBestFitFreeTable (Restaurant restaurant, int capacity, LocalDateTime start, LocalDateTime end);
}
