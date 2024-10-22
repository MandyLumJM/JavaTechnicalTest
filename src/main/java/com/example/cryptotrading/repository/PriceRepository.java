package com.example.cryptotrading.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cryptotrading.model.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
	
	Optional<Price> findTopByTradePairOrderByTimestampDesc(String tradePair);

	List<Price> findByTradePair(String tradePair);

}
