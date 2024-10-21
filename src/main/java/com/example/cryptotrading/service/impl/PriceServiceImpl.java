package com.example.cryptotrading.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.cryptotrading.model.BinancePriceResponse;
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

	private final WebClient webClient = WebClient.create();

	@Override
	@Scheduled(fixedRate = 10000) // 10 seconds interval
	public void fetchPrices() {
		logger.info("Fetching prices from external APIs...");
	
		fetchFromBinance();
		fetchFromHuobi();
	}

	private void fetchFromBinance() {
		logger.info("Fetching prices from Binance...");
		 
		String url = "https://api.binance.com/api/v3/ticker/bookTicker?symbol=ETHUSDT";
		try {
		BinancePriceResponse response = webClient.get().uri(url).retrieve().bodyToMono(BinancePriceResponse.class)
				.block();
		
		
		logger.info("Response from Binance >>> {} {} ",response.getBidPrice(), response.getAskPrice());

		savePrice("Binance", "ETHUSDT", response.getBidPrice(), response.getAskPrice());
		} catch (WebClientResponseException e) {
		    logger.error("Error fetching data from Binance API: {}", e.getMessage(), e);
		} catch (Exception e) {
		    logger.error("Unexpected error: {}", e.getMessage(), e);
		}
	}

	private void fetchFromHuobi() {
		logger.info("Fetching prices from Huobi...");
		
		String url = "https://api.huobi.pro/market/tickers";
		try {
		    HuobiPriceResponse response = webClient.get()
		            .uri(url)
		            .retrieve()
		            .bodyToMono(HuobiPriceResponse.class)
		            .block();

			if (response == null || response.getData() == null || response.getData().isEmpty()) {
				logger.warn("Received null or empty response from Huobi API");
			} else {
				for (HuobiTicker ticker : response.getData()) {
					if (ticker.getSymbol().equalsIgnoreCase("ethusdt")) {
						logger.info("Response from Huobi for ETHUSDT >>> {} {}", ticker.getBid(), ticker.getAsk());
						savePrice("Huobi", "ETHUSDT", ticker.getBid(), ticker.getAsk());
					} else if (ticker.getSymbol().equalsIgnoreCase("btcusdt")) {
						logger.info("Response from Huobi for BTCUSDT >>> {} {}", ticker.getBid(), ticker.getAsk());
						savePrice("Huobi", "BTCUSDT", ticker.getBid(), ticker.getAsk());
					}
				}
			}
		} catch (WebClientResponseException e) {
		    logger.error("Error fetching data from Huobi API: {}", e.getMessage(), e);
		} catch (Exception e) {
		    logger.error("Unexpected error: {}", e.getMessage(), e);
		}
		
	}

	private void savePrice(String exchange, String tradePair, String bidPrice, String askPrice) {
		Price price = new Price();
		price.setExchange(exchange);
		price.setTradePair(tradePair);
		price.setBidPrice(Double.parseDouble(bidPrice));
		price.setAskPrice(Double.parseDouble(askPrice));
		price.setTimestamp(LocalDateTime.now());

		priceRepository.save(price);
	}
	

    public Price getBestPrice(String tradePair) {
	
	 List<Price> prices = priceRepository.findByTradePair(tradePair);

     Double bestAsk = prices.stream().mapToDouble(Price::getAskPrice).min().orElse(0);
     Double bestBid = prices.stream().mapToDouble(Price::getBidPrice).max().orElse(0);

     Price bestPrice = new Price();
     bestPrice.setTradePair(tradePair);
     bestPrice.setAskPrice(bestAsk);
     bestPrice.setBidPrice(bestBid);

     return bestPrice;
    }
}