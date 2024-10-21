package com.example.cryptotrading.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cryptotrading.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>{

}
