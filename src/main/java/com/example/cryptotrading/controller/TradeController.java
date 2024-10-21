package com.example.cryptotrading.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptotrading.model.Trade;
import com.example.cryptotrading.model.TradeRequest;
import com.example.cryptotrading.service.TradeService;
import com.example.cryptotrading.service.impl.PriceServiceImpl;



@RestController
@RequestMapping("/api")
public class TradeController {
    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
    
	@Autowired
	TradeService tradeService;

	
	@PostMapping("/processtrade")
    public ResponseEntity<Trade> processTrade(@RequestParam Long userId, @RequestBody TradeRequest tradeRequest) {
		  logger.info("TradeController Entering processTrade...");
        Trade savedTrade = tradeService.processTrade(userId, tradeRequest);
      
        return ResponseEntity.ok(savedTrade);
    }

}
