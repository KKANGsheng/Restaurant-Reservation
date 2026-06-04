package com.smart.restaurantAppointment.controllers;

import com.smart.restaurantAppointment.Service.DltReplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DltAdminController {
    private final DltReplayService dltReplayService;

    @PostMapping("/replay/deadLetterMessage")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<Map<String,Object>> replay(@RequestParam String dltTopic, @RequestParam(defaultValue = "10") int maxMessages) {
        int count = dltReplayService.replayDeadLetterMessage(dltTopic, maxMessages);
        return ResponseEntity.ok(Map.of("dltTopic",dltTopic,"replayed",count));
    }

}
