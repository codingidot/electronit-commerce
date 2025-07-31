package kr.hhplus.be.server.dto.user;

import java.math.BigDecimal;

public class ChargeResponseDto {

	private BigDecimal balance;
	
	private ChargeResponseDto(BigDecimal balance) {
		this.balance = balance;
	}
	
	public static ChargeResponseDto toDto(BigDecimal balance) {
		return new ChargeResponseDto(balance);
	}
}
