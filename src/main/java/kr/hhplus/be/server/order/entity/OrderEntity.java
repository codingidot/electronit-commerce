package kr.hhplus.be.server.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.order.converter.OrderStateConverter;
import kr.hhplus.be.server.order.enums.OrderState;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
	@Convert(converter = OrderStateConverter.class)
    private OrderState orderState;
	LocalDateTime orderDate;
	
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
							totalPrice, count, OrderState.REQUEST, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
	}
}
