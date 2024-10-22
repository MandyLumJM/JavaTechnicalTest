package com.example.cryptotrading.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Wallet {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "currency", nullable = false, unique = true)
    private String currency;
    
    @Column(name = "balance", nullable = false)
    private Double balance;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
