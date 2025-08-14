package kr.hhplus.be.server.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.order.enums.OrderState;

public class OrderResponseDto {

	Long goodsId;
	Long userId;
	Long couponId;
	int count;
	BigDecimal orderPrice;
	BigDecimal payPrice;
	OrderState orderState;
	LocalDateTime orderDate;
	
	public OrderResponseDto(Long goodsId, Long userId, Long couponId, int count, BigDecimal orderPrice,
			BigDecimal payPrice, OrderState orderState, LocalDateTime orderDate) {
		this.goodsId = goodsId;
		this.userId = userId;
		this.couponId = couponId;
		this.count = count;
		this.orderPrice = orderPrice;
		this.payPrice = payPrice;
		this.orderState = orderState;
		this.orderDate = orderDate;
	}
}
