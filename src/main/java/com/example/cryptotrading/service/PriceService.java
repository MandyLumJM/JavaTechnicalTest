package com.example.cryptotrading.service;

import com.example.cryptotrading.model.Price;

public interface PriceService {
	
	public void fetchPrices();

	public Price getBestPrice(String tradePair);

}
