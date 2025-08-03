package kr.hhplus.be.server.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.user.entity.UserEntity;

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
	
	public static OrderEntity toEntity(UserEntity userInfo, ProductEntity buyProduct, int count, BigDecimal totalPrice, Long couponId) throws Exception {
		int stock = buyProduct.getStock();
		BigDecimal balance = userInfo.getBalance();
		Long goodsId = buyProduct.getGoodsId();
		
		if(balance.compareTo(totalPrice) < 0) {
			throw new Exception("잔액이 부족합니다.");
		}
		
		if(userInfo == null) {
			throw new Exception("유저 정보가 존재하지 않습니다.");
		}
		
		if(stock < count) {
			throw new Exception("재고가 부족합니다.");
		}
		
		return new OrderEntity(null, goodsId, couponId, userInfo.getUserId(), buyProduct.getPrice().multiply(BigDecimal.valueOf(count)),
							totalPrice, count, "10", LocalDateTime.now(ZoneId.of("Asia/Seoul")));
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
