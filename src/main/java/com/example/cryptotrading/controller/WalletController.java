package com.example.cryptotrading.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptotrading.model.Wallet;
import com.example.cryptotrading.service.TradeService;
import com.example.cryptotrading.service.WalletService;

/*
 * Fetch Wallet Details API
 */

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
	
	private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

   
    @GetMapping("/balances")
    public ResponseEntity<List<Wallet>> getWalletBalance(@RequestParam Long userId) {
        List<Wallet> walletBalance = walletService.getWalletBalance(userId);
        return ResponseEntity.ok(walletBalance);
    }
}