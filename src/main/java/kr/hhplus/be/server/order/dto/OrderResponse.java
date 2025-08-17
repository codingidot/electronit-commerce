package kr.hhplus.be.server.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.order.enums.OrderState;

public record OrderResponse(
	    Long goodsId,
	    Long userId,
	    Long couponId,
	    int count,
	    BigDecimal orderPrice,
	    BigDecimal payPrice,
	    OrderState orderState,
	    LocalDateTime orderDate
	) {}
