package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.Service.TableService;
import com.smart.restaurantAppointment.dto.Request.TableReq;
import com.smart.restaurantAppointment.dto.Request.UpdateTableReq;
import com.smart.restaurantAppointment.dto.ReservationRequestDTO;
import com.smart.restaurantAppointment.entity.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/table")
public class TableController {
    private final TableService tableService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<RestaurantTable> createTables(@Valid @RequestBody TableReq req) {
        RestaurantTable table = tableService.createTable(req);
        return  ResponseEntity.ok(table);
    }

    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<List<RestaurantTable>> getRestaurantTables(@PathVariable Long restaurantId) {
        List<RestaurantTable> tables = tableService.getRestaurantTables(restaurantId);
        return ResponseEntity.ok(tables);
    }

    @PutMapping("/{tableId}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<RestaurantTable> updateTable (@PathVariable Long tableId,@RequestBody UpdateTableReq req) {
        RestaurantTable table = tableService.updateTable(tableId, req);
        return ResponseEntity.ok(table);
    }

    @DeleteMapping("/{tableId}")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Void> deleteTable(@PathVariable Long tableId) {
        tableService.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }

}
