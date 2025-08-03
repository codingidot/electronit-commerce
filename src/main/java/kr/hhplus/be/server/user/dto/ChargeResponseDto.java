package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;

public class ChargeResponseDto {

	private BigDecimal balance;
	
	private ChargeResponseDto(BigDecimal balance) {
		this.balance = balance;
	}
	
	public static ChargeResponseDto toDto(BigDecimal balance) {
		return new ChargeResponseDto(balance);
	}

	public BigDecimal getBalance() {
		return balance;
	}
}
