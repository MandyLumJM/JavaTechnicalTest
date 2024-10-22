package com.example.cryptotrading.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptotrading.model.Trade;
import com.example.cryptotrading.model.TradeRequest;
import com.example.cryptotrading.service.TradeService;

/*
 *  Task 3 & 5
 *  allows users to trade based on the latest best aggregated price.
 *  the user trading history.
 */

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
    
	@Autowired
	TradeService tradeService;

	@PostMapping("/processtrade")
    public ResponseEntity<Trade> processTrade(@RequestParam Long userId, @RequestBody TradeRequest tradeRequest) {
		logger.info("TradeController processTrade...");
		
        Trade savedTrade = tradeService.processTrade(userId, tradeRequest);
        return ResponseEntity.ok(savedTrade);
    }
	
	@GetMapping("/history")
	public ResponseEntity<List<Trade>> fetchTradeHistory(@RequestParam Long userId) {
		logger.info("TradeController fetchTradeHistory...");
		
		List<Trade> tradeList = tradeService.fetchTradeHistory(userId);
        return ResponseEntity.ok(tradeList);
    }

}
