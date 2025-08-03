package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;


public class ChargeRequestDto {

	private Long userId;
	
	private BigDecimal amount;
	public ChargeRequestDto() {};
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
