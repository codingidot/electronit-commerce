package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;

public class BalanceResponseDto {

	private BigDecimal balance;
	
	public BalanceResponseDto(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
}
