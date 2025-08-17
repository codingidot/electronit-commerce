package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BalanceResponse {

	private BigDecimal balance;
}
