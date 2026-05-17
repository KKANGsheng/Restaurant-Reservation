package com.smart.restaurantAppointment.Service;


import com.smart.restaurantAppointment.dto.Request.TableReq;
import com.smart.restaurantAppointment.dto.Request.UpdateTableReq;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.Table;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableService {

    public Table createTable(TableReq req);

    public List<Table> getRestaurantTables(Long restaurantId);

    public Table updateTable(Long tableId, UpdateTableReq req);

    public void deleteTable(Long tableId);

    public Optional<Table> findBestFitFreeTable (Restaurant restaurant, int capacity, LocalDateTime start, LocalDateTime end);
}
