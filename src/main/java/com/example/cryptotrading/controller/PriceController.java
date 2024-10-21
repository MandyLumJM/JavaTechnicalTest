package com.example.cryptotrading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.cryptotrading.model.Price;
import com.example.cryptotrading.repository.PriceRepository;
import com.example.cryptotrading.service.PriceService;

@RestController
@RequestMapping("/api")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/best-price")
    public Price getBestPrice(@RequestParam String tradePair) {

    	Price bestPrice = priceService.getBestPrice(tradePair);
    	
        return bestPrice;
    }
}