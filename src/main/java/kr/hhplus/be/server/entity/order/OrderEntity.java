package kr.hhplus.be.server.entity.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long orderId;
	Long goodsId; 
	Long couponId; 
	Long userId;
	BigDecimal orderPrice; 
	BigDecimal payPrice; 
	int count; 
	String orderState;
	LocalDateTime orderDate;
	
	OrderEntity(){};
	
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

	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	
}
