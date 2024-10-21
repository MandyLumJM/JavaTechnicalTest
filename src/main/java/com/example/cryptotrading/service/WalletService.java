package com.example.cryptotrading.service;

import java.util.List;

import com.example.cryptotrading.model.Wallet;

public interface WalletService {
	
	public List<Wallet> getWalletBalance(Long userId);
}
