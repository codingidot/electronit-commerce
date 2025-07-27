package kr.hhplus.be.server.repository.order;

import kr.hhplus.be.server.dto.order.Order;

public interface OrderRepository {

	void insertOrderInfo(Order orderDto);
	Long getOrderSeq();
}
