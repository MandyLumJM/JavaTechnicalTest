package com.example.cryptotrading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cryptotrading.model.Trade;
import com.example.cryptotrading.model.TradeRequest;


public interface TradeService {

	public Trade processTrade(Long userId, TradeRequest tradeRequest);
 
}
