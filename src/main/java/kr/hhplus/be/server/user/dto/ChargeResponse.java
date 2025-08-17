package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChargeResponse {

	private BigDecimal balance;

	public static ChargeResponse toDto(BigDecimal balance) {
		return new ChargeResponse(balance);
	}
}
