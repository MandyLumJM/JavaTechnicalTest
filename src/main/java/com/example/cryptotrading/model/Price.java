package com.example.cryptotrading.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "PRICE")
public class Price {
   

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exchange; 
    private String tradePair; 
    private Double bidPrice; 
    private Double askPrice; 

    private LocalDateTime timestamp; 
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
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

}
