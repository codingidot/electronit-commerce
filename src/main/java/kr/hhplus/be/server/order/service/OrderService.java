package kr.hhplus.be.server.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.order.entity.OrderEntity;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.user.entity.UserEntity;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	OrderService(OrderRepository orderRepository){
		this.orderRepository = orderRepository;
	}

	public void insertOrder(OrderEntity order) {
		orderRepository.insertOrderInfo(order);
	}

	//주문생성
	public void createOrder(UserEntity userInfo, ProductEntity buyProduct, int count, BigDecimal totalPrice, Long couponId) throws Exception {
		//주문 테이블에 insert
		OrderEntity order = OrderEntity.toEntity(userInfo, buyProduct, count, totalPrice, couponId);
		this.insertOrder(order);
	}

}
