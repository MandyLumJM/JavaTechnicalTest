package com.example.cryptotrading.model;

import java.util.List;

import lombok.Data;

@Data
public class HuobiPriceResponse {

	private List<HuobiTicker> data;

	public List<HuobiTicker> getData() {
		return data;
	}

	public void setData(List<HuobiTicker> data) {
		this.data = data;
	}

	
	 
	 
}
