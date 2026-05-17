package com.smart.restaurantAppointment.Service.impl;

import com.smart.restaurantAppointment.Enumerator.ReservationStatus;
import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.Service.RestaurantService;
import com.smart.restaurantAppointment.Service.TableService;
import com.smart.restaurantAppointment.dto.Request.TableReq;
import com.smart.restaurantAppointment.dto.Request.UpdateTableReq;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.entity.Restaurant;
import com.smart.restaurantAppointment.entity.Table;
import com.smart.restaurantAppointment.repository.ReservationRepository;
import com.smart.restaurantAppointment.repository.RestaurantRepository;
import com.smart.restaurantAppointment.repository.TableRepository;
import com.smart.restaurantAppointment.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public Table createTable(TableReq req) {
        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId())
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));
        restaurantService.validateRestaurantOwnership(restaurant);
        Table table = new Table();
        table.setCapacity(req.getCapacity());
        table.setName(req.getName());
        table.setRestaurant(restaurant);
        tableRepository.save(table);
        return table;
    }

    @Override
    public List<Table> getRestaurantTables(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));
        restaurantService.validateRestaurantOwnership(restaurant);
        List<Table> table = tableRepository.findByRestaurant(restaurant);
        return table;
    }

    @Override
    public Table updateTable(Long tableId, UpdateTableReq req) {
        Table table = tableRepository.findById(tableId).orElseThrow(() -> new BadRequestException("Table did not exist"));
        restaurantService.validateRestaurantOwnership(table.getRestaurant());
        table.setName(req.getName());
        table.setCapacity(req.getCapacity());
        tableRepository.save(table);
        return table;
    }

    @Override
    public void deleteTable(Long tableId) {
        Table table = tableRepository.findById(tableId).orElseThrow(()-> new BadRequestException("Table did not exist"));
        restaurantService.validateRestaurantOwnership(table.getRestaurant());
        tableRepository.delete(table);
    }

    @Override
    public Optional<Table> findBestFitFreeTable(Restaurant restaurant, int capacity, LocalDateTime start, LocalDateTime end) {
        List<Table> availableTables = tableRepository.findByRestaurant(restaurant);
        return availableTables.stream()
                .filter(t->t.getCapacity() != null && t.getCapacity() >=capacity)
                .filter(t-> reservationRepository.findOverlappingRestaurant(t,start,end, ReservationStatus.CANCELED).isEmpty())
                .sorted(Comparator.comparingInt(Table::getCapacity))
                .findFirst();
    }

}
