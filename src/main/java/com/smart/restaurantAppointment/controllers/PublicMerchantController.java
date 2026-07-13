package com.smart.restaurantAppointment.controllers;

import com.smart.restaurantAppointment.Exception.BadRequestException;
import com.smart.restaurantAppointment.dto.PublicBrandingDTO;
import com.smart.restaurantAppointment.dto.UserDTO;
import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicMerchantController {
    private final MerchantRepository merchantRepository;

    @GetMapping("/branding/{slug}")
    public ResponseEntity<PublicBrandingDTO> getSlug(@PathVariable String slug) {
        Merchant m = merchantRepository.findBySlug(slug).orElseThrow(()-> new BadRequestException("Unknown Merchant"));
        return ResponseEntity.ok(PublicBrandingDTO.from(m));
    }

//    @PostMapping("/register/{slug}")
//    public ResponseEntity<UserDTO> register(@PathVariable String slug, @RequestBody UserDTO userDTO) {
//
//    }

}
