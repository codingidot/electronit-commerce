package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChargeResponseDto {

	private BigDecimal balance;

	public static ChargeResponseDto toDto(BigDecimal balance) {
		return new ChargeResponseDto(balance);
	}
}
