package com.example.cryptotrading.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRADE")
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "tradePair", nullable = false)
	private String tradePair;
	
	@Column(name = "amount", nullable = false)
	private Double amount;
	
	@Column(name = "price", nullable = false)
	private Double price;
	
	@Column(name = "TradeType", nullable = false)
	private String TradeType; 
	
	@Column(name = "transactionTime", nullable = false)
	private LocalDateTime transactionTime;
	
	@Column(name = "userId", nullable = false)
	private Long userId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradePair() {
		return tradePair;
	}

	public void setTradePair(String tradePair) {
		this.tradePair = tradePair;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTradeType() {
		return TradeType;
	}

	public void setTradeType(String tradeType) {
		TradeType = tradeType;
	}

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
}
