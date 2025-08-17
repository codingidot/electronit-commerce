package kr.hhplus.be.server.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderRequestDto {

	Long userId;
	Long goodsId;
	int count;
	private Long couponId;
	
}
