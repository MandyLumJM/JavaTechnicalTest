package com.example.cryptotrading.model;

import lombok.Data;

@Data
public class HuobiTicker {
    private String symbol;
    private String bid;
    private String ask;
    
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getAsk() {
		return ask;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	
	

	  
    
    
	
    
}

