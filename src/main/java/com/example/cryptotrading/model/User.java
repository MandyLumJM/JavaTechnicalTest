package com.example.cryptotrading.model;


import jakarta.persistence.*;

@Entity
@Table(name = "APP_USER") // user is a reserved keyword in H2 database 
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	 @Column(name = "username", nullable = false, unique = true)
    private String username;
    
	  @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "wallet_balance", nullable = false)
    private Double walletBalance;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(Double walletBalance) {
		this.walletBalance = walletBalance;
	}
    
    
}
