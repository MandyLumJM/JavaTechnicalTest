package com.example.cryptotrading.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "PRICE")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "tradePair", nullable = false)
	private String tradePair;
	
	@Column(name = "bidPrice", nullable = false)
	private Double bidPrice;
	
	@Column(name = "askPrice", nullable = false)
	private Double askPrice;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

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

	public Double getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(Double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public Double getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(Double askPrice) {
		this.askPrice = askPrice;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		return "Price [id=" + id + ", tradePair=" + tradePair + ", bidPrice=" + bidPrice + ", askPrice=" + askPrice
				+ ", timestamp=" + timestamp + "]";
	}

	public Price(String tradePair, Double bidPrice, Double askPrice) {
		this.tradePair = tradePair;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
	}

	public Price() {
		
	}
}
