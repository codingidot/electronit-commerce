package kr.hhplus.be.server.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChargeRequest {

	private Long userId;
	
	private BigDecimal amount;
}
