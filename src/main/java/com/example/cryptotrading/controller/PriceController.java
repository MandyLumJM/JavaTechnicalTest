package com.example.cryptotrading.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cryptotrading.model.Price;
import com.example.cryptotrading.service.PriceService;

@RestController
@RequestMapping("/api")
public class PriceController {
	private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

	@Autowired
	private PriceService priceService;

	@GetMapping("/bestprice")
	public ResponseEntity<Price> getBestPrice(@RequestParam String tradePair) {
		logger.info("PriceController getBestPrice...");
		
		Price bestPrice = priceService.getBestPrice(tradePair);
		return ResponseEntity.ok(bestPrice);
	}
}