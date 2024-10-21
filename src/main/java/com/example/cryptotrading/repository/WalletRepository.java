package com.example.cryptotrading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptotrading.model.User;
import com.example.cryptotrading.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Optional<Wallet> findByUserIdAndCurrency(Long userId, String currency);
}
