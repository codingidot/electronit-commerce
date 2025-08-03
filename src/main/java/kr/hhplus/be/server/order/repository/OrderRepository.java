package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.entity.OrderEntity;

public interface OrderRepository {

	void insertOrderInfo(OrderEntity order);
}
