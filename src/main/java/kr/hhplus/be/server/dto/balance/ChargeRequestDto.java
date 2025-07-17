package kr.hhplus.be.server.dto.balance;

import java.math.BigDecimal;

public class ChargeRequestDto {

	private Long userId;
	private BigDecimal amount;
	
	public Long getUserId() {
		return userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	
	
}
