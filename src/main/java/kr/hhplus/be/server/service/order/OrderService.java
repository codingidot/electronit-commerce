package kr.hhplus.be.server.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.entity.order.OrderEntity;
import kr.hhplus.be.server.entity.product.ProductEntity;
import kr.hhplus.be.server.entity.user.UserEntity;
import kr.hhplus.be.server.repository.order.OrderRepository;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	OrderService(OrderRepository orderRepository){
		this.orderRepository = orderRepository;
	}

	public void insertOrder(OrderEntity order) {
		orderRepository.insertOrderInfo(order);
	}
	
	public Long getOrderSeq() {
		return orderRepository.getOrderSeq();
	}

	//주문생성
	public void createOrder(UserEntity userInfo, ProductEntity buyProduct, int count, BigDecimal totalPrice, Long couponId) throws Exception {
		//주문 테이블에 insert
		Long orderNewId = this.getOrderSeq();
		OrderEntity order = OrderEntity.toEntity(userInfo, buyProduct, count, totalPrice, couponId);
		this.insertOrder(order);
	}

}
