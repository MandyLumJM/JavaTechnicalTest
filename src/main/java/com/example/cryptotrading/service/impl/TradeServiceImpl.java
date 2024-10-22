package com.example.cryptotrading.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cryptotrading.Exceptions.CustomException;

import com.example.cryptotrading.model.Price;
import com.example.cryptotrading.model.Trade;
import com.example.cryptotrading.model.TradeRequest;
import com.example.cryptotrading.model.User;
import com.example.cryptotrading.model.Wallet;
import com.example.cryptotrading.service.TradeService;

import com.example.cryptotrading.repository.PriceRepository;
import com.example.cryptotrading.repository.TradeRepository;
import com.example.cryptotrading.repository.UserRepository;
import com.example.cryptotrading.repository.WalletRepository;


@Service
public class TradeServiceImpl implements TradeService {

	private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

	@Autowired
	private TradeRepository tradeRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PriceRepository priceRepository;

	/*
	 * Trade Execution logic (BUY/SELL) with latest price 
	 */
	@Transactional
	@Override
	public Trade processTrade(Long userId, TradeRequest tradeRequest) {
		logger.info("TradeServiceImpl Entering processTrade >>> TradeType = {} Based = {} Quote = {} ", tradeRequest.getTradeType(), tradeRequest.getBaseCurrency(),tradeRequest.getQuoteCurrency());

		try {

			Price latestPrice = priceRepository.findTopByTradePairOrderByTimestampDesc(tradeRequest.getTradePair())
					.orElseThrow(() -> new CustomException("Latest price for trade pair not available."));

			User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found."));

			Wallet baseCurrency = walletRepository.findByUserIdAndCurrency(userId, tradeRequest.getBaseCurrency())
					.orElseThrow(() -> new CustomException(
							"User wallet for currency " + tradeRequest.getBaseCurrency() + " not found."));
			
			Wallet quoteCurrency = walletRepository.findByUserIdAndCurrency(userId, tradeRequest.getQuoteCurrency())
					.orElseThrow(() -> new CustomException(
							"User wallet for currency " + tradeRequest.getQuoteCurrency() + " not found."));

			if (baseCurrency == null && quoteCurrency == null) {
				throw new CustomException("User wallet not found.");
			}
			

			double tradePrice = determineTradePrice(tradeRequest.getTradeType(), latestPrice);
			double totalTradeCost = tradeRequest.getAmount() * tradePrice;
			
			// Check if wallet has sufficient funds
			if (tradeRequest.getTradeType().equalsIgnoreCase("buy")) {
				logger.info("TradeServiceImpl processTrade >>> TotalCost = {} Balance = {} ", totalTradeCost, quoteCurrency.getBalance());
				if (quoteCurrency.getBalance() < totalTradeCost) {
					throw new CustomException("Insufficient wallet balance to execute the trade.");
				}
			}

			Trade trade = new Trade();
			trade.setUserId(userId);
			trade.setTradePair(tradeRequest.getTradePair());
			trade.setAmount(tradeRequest.getAmount());
			trade.setPrice(tradePrice);
			trade.setTradeType(tradeRequest.getTradeType());
			trade.setTransactionTime(LocalDateTime.now());
			
			Trade savedTrade = tradeRepository.save(trade);

			updateWallet(savedTrade,baseCurrency,quoteCurrency,totalTradeCost,user,tradeRequest);
			
			logger.info("TradeServiceImpl Exiting processTrade >>>>>>");
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
		logger.info("TradeServiceImpl determineTradePrice >>> TradeType = {} Bid = {} Ask = {}", tradeType,latestPrice.getBidPrice(), latestPrice.getAskPrice());
		return tradeType.equalsIgnoreCase("buy") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
	}
	
	private void updateWallet(Trade savedTrade, Wallet baseCurrency, Wallet quoteCurrency, double totalTradeCost, User user, TradeRequest tradeRequest) {
		logger.info("TradeServiceImpl updateWallet >>>");
		if (savedTrade != null && savedTrade.getId() != null) {
			if (savedTrade.getTradeType().equalsIgnoreCase("buy")) {
				baseCurrency.setBalance(baseCurrency.getBalance() + tradeRequest.getAmount());
				quoteCurrency.setBalance(quoteCurrency.getBalance() - totalTradeCost);
				
				user.setWalletBalance(user.getWalletBalance() - totalTradeCost);
				
			} else if (savedTrade.getTradeType().equalsIgnoreCase("sell")) {
				baseCurrency.setBalance(baseCurrency.getBalance() - tradeRequest.getAmount());
				quoteCurrency.setBalance(quoteCurrency.getBalance() + totalTradeCost);
				user.setWalletBalance(user.getWalletBalance() + totalTradeCost);
				
			}
			walletRepository.save(quoteCurrency);
			walletRepository.save(baseCurrency);
			userRepository.save(user);
			logger.info("TradeServiceImpl wallet updated >>>");
		} else {
			throw new CustomException("Failed to save the trade. Transaction aborted.");
		}
	}

	@Override
	public List<Trade> fetchTradeHistory(Long userId) {
		logger.info("TradeServiceImpl fetchTradeHistory >>> {}", userId);
		List<Trade> tradeList = tradeRepository.findByUserId(userId);
		return tradeList;
	}
	
	
}
