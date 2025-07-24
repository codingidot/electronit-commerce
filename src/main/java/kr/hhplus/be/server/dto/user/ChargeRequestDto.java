package kr.hhplus.be.server.dto.user;

import java.math.BigDecimal;


public class ChargeRequestDto {

	private Long userId;
	
	private BigDecimal amount;
	
	public ChargeRequestDto(Long userId, BigDecimal amount) {
		this.userId = userId;
		this.amount = amount;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
}
