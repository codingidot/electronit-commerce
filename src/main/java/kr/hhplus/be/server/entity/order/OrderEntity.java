package kr.hhplus.be.server.entity.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.order.Order;

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
	
	protected OrderEntity(){};
	private OrderEntity(Long orderId, Long goodsId, Long couponId, Long userId, BigDecimal orderPrice,
			BigDecimal payPrice, int count, String orderState, LocalDateTime orderDate) {
		this.orderId = orderId;
		this.goodsId = goodsId;
		this.couponId = couponId;
		this.userId = userId;
		this.orderPrice = orderPrice;
		this.payPrice = payPrice;
		this.count = count;
		this.orderState = orderState;
		this.orderDate = orderDate;
	}

	public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity(null, order.getGoodsId(), order.getCouponId(), order.getUserId(), order.getOrderPrice(),
        						order.getPayPrice(), order.getCount(), order.getOrderState(), order.getOrderDate());
        return entity;
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

	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	
}
