package kr.hhplus.be.server.order.repository;

import java.util.Optional;

import kr.hhplus.be.server.order.entity.OrderEntity;

public interface OrderRepository {

	void insertOrderInfo(OrderEntity order);
	Optional<OrderEntity> getOrderInfo(Long goodsId);
}
