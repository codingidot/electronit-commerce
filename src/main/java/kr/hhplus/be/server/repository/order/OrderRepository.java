package kr.hhplus.be.server.repository.order;

import kr.hhplus.be.server.dto.order.OrderDto;

public interface OrderRepository {

	void insertOrderInfo(OrderDto orderDto);
	Long getOrderSeq();
}
