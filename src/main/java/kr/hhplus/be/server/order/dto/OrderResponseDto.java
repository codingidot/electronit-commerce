package kr.hhplus.be.server.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.order.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderResponseDto {

	Long goodsId;
	Long userId;
	Long couponId;
	int count;
	BigDecimal orderPrice;
	BigDecimal payPrice;
	OrderState orderState;
	LocalDateTime orderDate;
}
