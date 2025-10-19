package com.smart.restaurantAppointment.controllers;


import com.smart.restaurantAppointment.entity.Merchant;
import com.smart.restaurantAppointment.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantRepository merchantRepository;

    @GetMapping("getAllMerchant")
    public List<Merchant> getAllMerchants(){
        return merchantRepository.findAll();
    }
}
