package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.Service.TableService;
import com.smart.restaurantAppointment.dto.request.TableReq;
import com.smart.restaurantAppointment.dto.request.UpdateTableReq;
import com.smart.restaurantAppointment.dto.RestaurantTableDTO;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.RestaurantTable;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.repository.RestaurantTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;

@AllArgsConstructor
@Service
public class TableServiceImpl implements TableService {
    private final RestaurantTableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public RestaurantTable createTable(TableReq req) {
        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId())
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));
        restaurantService.validateRestaurantOwnership(restaurant);
        RestaurantTable table = new RestaurantTable();
        table.setCapacity(req.getCapacity());
        table.setName(req.getName());
        table.setRestaurant(restaurant);
        tableRepository.save(table);
        return table;
    }

    @Override
    public List<RestaurantTableDTO> getRestaurantTables(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));
        restaurantService.validateRestaurantOwnership(restaurant);
        List<RestaurantTableDTO> tables = tableRepository.findByRestaurant(restaurant)
                                                    .stream().map(RestaurantTableDTO::from).toList();
        return tables;
    }

    @Override
    public RestaurantTable updateTable(Long tableId, UpdateTableReq req) {
        RestaurantTable table = tableRepository.findById(tableId).orElseThrow(() -> new BadRequestException("Table did not exist"));
        restaurantService.validateRestaurantOwnership(table.getRestaurant());
        table.setName(req.getName());
        table.setCapacity(req.getCapacity());
        tableRepository.save(table);
        return table;
    }

    @Override
    public void deleteTable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId).orElseThrow(()-> new BadRequestException("Table did not exist"));
        restaurantService.validateRestaurantOwnership(table.getRestaurant());
        tableRepository.delete(table);
    }

    @Override
    public Optional<RestaurantTable> findBestFitFreeTable(Restaurant restaurant, int capacity, LocalDateTime start, LocalDateTime end) {
        return tableRepository
                .findFreeTablesFittingSize(restaurant, capacity, start, end, PageRequest.of(0,1))
                .stream().findFirst();
    }

}
