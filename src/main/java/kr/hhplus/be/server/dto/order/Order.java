package kr.hhplus.be.server.dto.order;

import java.math.BigDecimal;

public class Order {

	Long orderId;
	Long goodsId; 
	Long couponId; 
	Long userId;
	BigDecimal orderPrice; 
	BigDecimal payPrice; 
	int count; 
	String orderState;
	
	public Order(Long orderId, Long goodsId, Long couponId, Long userId, BigDecimal orderPrice, BigDecimal payPrice,
			int count, String orderState) {
		this.orderId = orderId;
		this.goodsId = goodsId;
		this.couponId = couponId;
		this.userId = userId;
		this.orderPrice = orderPrice;
		this.payPrice = payPrice;
		this.count = count;
		this.orderState = orderState;
	}
	public Long getOrderId() {
		return orderId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public Long getCouponId() {
		return couponId;
	}
	public Long getUserId() {
		return userId;
	}
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public BigDecimal getPayPrice() {
		return payPrice;
	}
	public int getCount() {
		return count;
	}
	public String getOrderState() {
		return orderState;
	}
}
