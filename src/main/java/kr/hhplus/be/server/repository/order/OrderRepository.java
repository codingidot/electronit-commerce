package kr.hhplus.be.server.repository.order;

import kr.hhplus.be.server.entity.order.OrderEntity;

public interface OrderRepository {

	void insertOrderInfo(OrderEntity order);
	Long getOrderSeq();
}
