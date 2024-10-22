package com.example.cryptotrading.service;

import java.util.List;

import com.example.cryptotrading.model.Trade;
import com.example.cryptotrading.model.TradeRequest;


public interface TradeService {

	public Trade processTrade(Long userId, TradeRequest tradeRequest);

	public List<Trade> fetchTradeHistory(Long userId);
 
}
