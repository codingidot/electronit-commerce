package kr.hhplus.be.server.repository.order;

import java.util.HashMap;
import java.util.Map;

import kr.hhplus.be.server.dto.order.OrderDto;

public class OrderMemoryRepository implements OrderRepository{

	public Map<Long, OrderDto> orderTable = new HashMap<>();
	public Long orderSequence = 1L;
	
	@Override
	public void insertOrderInfo(OrderDto order) {
		orderTable.put(order.getOrderId(), order);
	}
	
	public Long getOrderSeq(){
		Long seq = orderSequence;
		orderSequence++;
		return seq;
	}

}
