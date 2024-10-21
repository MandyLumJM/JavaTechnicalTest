package com.example.cryptotrading.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.cryptotrading.Exceptions.CustomException;
import com.example.cryptotrading.model.BinancePriceResponse;
import com.example.cryptotrading.model.Price;
import com.example.cryptotrading.model.Trade;
import com.example.cryptotrading.model.TradeRequest;
import com.example.cryptotrading.model.User;
import com.example.cryptotrading.model.Wallet;
import com.example.cryptotrading.service.PriceService;
import com.example.cryptotrading.service.TradeService;
import com.example.cryptotrading.repository.PriceRepository;
import com.example.cryptotrading.repository.TradeRepository;
import com.example.cryptotrading.repository.UserRepository;
import com.example.cryptotrading.repository.WalletRepository;

/*
 * Trade Execution logic (BUY/SELL) with latest price 
 */

@Service
public class TradeServiceImpl implements TradeService {
	// REST APIs to handle buy/sell trades.

	private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

	@Autowired
	private TradeRepository tradeRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PriceRepository priceRepository; // Inject the PriceRepository

	@Transactional
	@Override
	public Trade processTrade(Long userId, TradeRequest tradeRequest) {
		logger.info("TradeServiceImpl Entering processTrade >>> {} {} ", tradeRequest.getTradeType(), tradeRequest.getCurrency());

		try {
			Double currentPrice = 0.0;

			Price latestPrice = priceRepository.findTopByTradePairOrderByTimestampDesc(tradeRequest.getTradePair())
					.orElseThrow(() -> new CustomException("Latest price for trade pair not available."));

			User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found."));

			Wallet wallet = walletRepository.findByUserIdAndCurrency(userId, tradeRequest.getCurrency())
					.orElseThrow(() -> new CustomException(
							"User wallet for currency " + tradeRequest.getCurrency() + " not found."));

			if (wallet == null) {
				throw new CustomException("User wallet not found.");
			}

			double tradePrice = determineTradePrice(tradeRequest.getTradeType(), latestPrice);
			double totalTradeCost = tradeRequest.getAmount() * tradePrice;

			if (tradeRequest.getTradeType().equalsIgnoreCase("buy")) {
				currentPrice = latestPrice.getAskPrice();

				if (wallet.getBalance() < totalTradeCost) {
					throw new CustomException("Insufficient wallet balance to execute the trade.");
				}
			}

			Trade trade = new Trade();
			trade.setUserId(userId);
			trade.setTradePair(tradeRequest.getTradePair());
			trade.setAmount(tradeRequest.getAmount());
			trade.setPrice(currentPrice);
			trade.setTradeType(tradeRequest.getTradeType());
			trade.setTransactionTime(LocalDateTime.now());

			Trade savedTrade = tradeRepository.save(trade);

			if (savedTrade != null && savedTrade.getId() != null) {
				if (trade.getTradeType().equalsIgnoreCase("buy")) {
					wallet.setBalance(wallet.getBalance() - totalTradeCost);
					user.setWalletBalance(user.getWalletBalance() - totalTradeCost);
				} else if (trade.getTradeType().equalsIgnoreCase("sell")) {
					wallet.setBalance(wallet.getBalance() + (trade.getAmount() * trade.getPrice()));
					user.setWalletBalance(user.getWalletBalance() + (trade.getAmount() * trade.getPrice()));
				}
				walletRepository.save(wallet);
				userRepository.save(user);

			} else {
				throw new CustomException("Failed to save the trade. Transaction aborted.");
			}

			return savedTrade;

		} catch (DataIntegrityViolationException e) {
			// Handle data integrity violation (e.g., database constraints)
			throw new CustomException("Trade could not be processed due to data integrity violation.", e);
		} catch (Exception e) {
			// Catch general exceptions
			throw new CustomException("An error occurred while processing the trade.", e);
		}
	}

	private double determineTradePrice(String tradeType, Price latestPrice) {
		logger.info("TradeServiceImpl determineTradePrice >>> {} {} ", tradeType, latestPrice);

		return tradeType.equalsIgnoreCase("buy") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
	}
}
