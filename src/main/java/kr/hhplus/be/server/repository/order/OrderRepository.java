package kr.hhplus.be.server.repository.order;

import kr.hhplus.be.server.domain.order.Order;

public interface OrderRepository {

	void insertOrderInfo(Order orderDto);
	Long getOrderSeq();
}
