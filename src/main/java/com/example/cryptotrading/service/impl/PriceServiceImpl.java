package com.example.cryptotrading.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.cryptotrading.Exceptions.CustomException;
import com.example.cryptotrading.model.BinanceTicker;
import com.example.cryptotrading.model.HuobiPriceResponse;
import com.example.cryptotrading.model.HuobiTicker;
import com.example.cryptotrading.model.Price;
import com.example.cryptotrading.repository.PriceRepository;
import com.example.cryptotrading.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PriceServiceImpl implements PriceService {

	private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

	@Autowired
	private PriceRepository priceRepository;

	private WebClient webClient = WebClient.create();
	
	

	// Increase webClient memory to handle larger response load
	public PriceServiceImpl(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://api.binance.com")
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // Set to 10MB				
						.build())
				.build();
	}
	
	/*
	 * Task 1: 10 seconds interval scheduler to retrieve the pricing from the source above and store the best pricing into the database.
	 */

	@Override
	@Scheduled(fixedRate = 10000) // 10 seconds interval
	public void fetchPrices() {
		BinanceTicker[] binanceResponse = fetchFromBinance();
		HuobiPriceResponse Huobiresponse = fetchFromHuobi();
		findBestPrices(binanceResponse,Huobiresponse);
	}

	public BinanceTicker[] fetchFromBinance() {
		
		logger.info("Fetching prices from Binance...");

		String url = "/api/v3/ticker/bookTicker";
		try {
			BinanceTicker[] response = webClient.get().uri(url).retrieve().bodyToMono(BinanceTicker[].class).block();

			if (response == null || response.length == 0) {
				logger.warn("Received null or empty response from Binance API");
			} else {
				return response;
			}
		} catch (WebClientResponseException e) {
			logger.error("Error fetching data from Binance API: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage(), e);
		}
		return null;
	}

	private HuobiPriceResponse fetchFromHuobi() {
		logger.info("Fetching prices from Huobi...");

		String url = "https://api.huobi.pro/market/tickers";
		try {
			HuobiPriceResponse response = webClient.get().uri(url).retrieve().bodyToMono(HuobiPriceResponse.class)
					.block();

			if (response == null || response.getData() == null || response.getData().isEmpty()) {
				logger.warn("Received null or empty response from Huobi API");
			} else {
				return response;
			}
		} catch (WebClientResponseException e) {
			logger.error("Error fetching data from Huobi API: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage(), e);
		}
		return null;

	}
	
	private void findBestPrices(BinanceTicker[] binanceResponse, HuobiPriceResponse Huobiresponse) {
		
		List<Price> ethPrices = new ArrayList<>();
		List<Price> btcPrices = new ArrayList<>();

		for (BinanceTicker bTicker : binanceResponse) {
			if (bTicker.getSymbol().equalsIgnoreCase("ethusdt")) {
				logger.info("Response from Binance for ETHUSDT >>> Bid = {} ASK = {}", bTicker.getBidPrice(),bTicker.getAskPrice());
				ethPrices.add(new Price("ETHUSDT", Double.parseDouble(bTicker.getBidPrice()), Double.parseDouble(bTicker.getAskPrice())));
	
			} else if (bTicker.getSymbol().equalsIgnoreCase("btcusdt")) {
				logger.info("Response from Binance for BTCUSDT >>> Bid = {} ASK = {}", bTicker.getBidPrice(),bTicker.getAskPrice());
				btcPrices.add(new Price("BTCUSDT", Double.parseDouble(bTicker.getBidPrice()), Double.parseDouble(bTicker.getAskPrice())));
			}
		}
		
		for (HuobiTicker hTicker : Huobiresponse.getData()) {
			if (hTicker.getSymbol().equalsIgnoreCase("ethusdt")) {
				logger.info("Response from Huobi for ETHUSDT >>> Bid = {} ASK = {}", hTicker.getBid(),hTicker.getAsk());
				ethPrices.add(new Price("ETHUSDT", Double.parseDouble(hTicker.getBid()), Double.parseDouble(hTicker.getAsk())));

			} else if (hTicker.getSymbol().equalsIgnoreCase("btcusdt")) {
				logger.info("Response from Huobi for BTCUSDT >>> Bid = {} ASK = {}", hTicker.getBid(),hTicker.getAsk());
				btcPrices.add(new Price("BTCUSDT", Double.parseDouble(hTicker.getBid()), Double.parseDouble(hTicker.getAsk())));
			}
		}
	
		// Find best ETH Price
		Price bestEthBidPrice = ethPrices.stream().max(Comparator.comparing(Price::getBidPrice)).orElse(null);
		Price bestEthAskPrice = ethPrices.stream().min(Comparator.comparing(Price::getAskPrice)).orElse(null);

		if (bestEthAskPrice != null && bestEthBidPrice != null) {
			logger.info("Best ETH Price >>> Bid = {} FROM {} ", bestEthBidPrice.getBidPrice());
			logger.info("Best ETH Price >>> ASK = {} FROM {}", bestEthAskPrice.getAskPrice());
			
			savePrice(bestEthBidPrice.getBidPrice(),bestEthAskPrice.getAskPrice(),"ETHUSDT");
		
		}
		
		// Find best BTC Price
		Price bestBtcBidPrice = btcPrices.stream().max(Comparator.comparing(Price::getBidPrice)).orElse(null);
		Price bestBtcAskPrice = btcPrices.stream().min(Comparator.comparing(Price::getAskPrice)).orElse(null);

		if (bestBtcAskPrice != null && bestBtcBidPrice != null) {
			logger.info("Best BTC Price >>> Bid = {} FROM {}", bestBtcBidPrice.getBidPrice());
			logger.info("Best BTC Price >>> ASK = {} FROM {}", bestBtcAskPrice.getAskPrice());
			
			savePrice(bestBtcBidPrice.getBidPrice() ,bestBtcAskPrice.getAskPrice(),"BTCUSDT");
		}
	}
	
	private void savePrice(Double bestBidPrice, Double bestAskPrice, String tradePair) {
		Price bestPrice = new Price();
		bestPrice.setBidPrice(bestBidPrice);
		bestPrice.setAskPrice(bestAskPrice);
		bestPrice.setTradePair(tradePair);
		bestPrice.setTimestamp(LocalDateTime.now());
		
		logger.info("Saving Best Prices >>> {}",bestPrice);
		priceRepository.save(bestPrice);
	}

	public Price getBestPrice(String tradePair) {
		
		Price price = priceRepository.findTopByTradePairOrderByTimestampDesc(tradePair)
				.orElseThrow(() -> new CustomException("price for trade pair not available."));

		
		logger.info("Best Price TradePair = {} BestPrice = {}",tradePair,price);

		Price bestPrice = new Price();
		bestPrice.setTradePair(tradePair);
		bestPrice.setBidPrice(price.getBidPrice());
		bestPrice.setAskPrice(price.getAskPrice());

		return bestPrice;
	}
}