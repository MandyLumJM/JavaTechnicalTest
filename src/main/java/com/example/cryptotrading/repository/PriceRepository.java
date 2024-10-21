package com.example.cryptotrading.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cryptotrading.model.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

	List<Price> findByTradePair(String tradePair);

}