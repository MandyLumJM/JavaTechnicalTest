package com.example.cryptotrading.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cryptotrading.model.Wallet;
import com.example.cryptotrading.repository.WalletRepository;
import com.example.cryptotrading.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

	private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

	@Autowired
	WalletRepository walletRepository;

	@Override
	public List<Wallet> getWalletBalance(Long userId) {
		logger.info("WalletServiceImpl Fetching getWalletBalance >>> {}", userId);

		return walletRepository.findByUserId(userId);
	}

}
