package kr.hhplus.be.server.repository.order;

import java.util.HashMap;
import java.util.Map;

import kr.hhplus.be.server.domain.order.Order;

public class OrderMemoryRepository implements OrderRepository{

	public Map<Long, Order> orderTable = new HashMap<>();
	public Long orderSequence = 1L;
	
	@Override
	public void insertOrderInfo(Order order) {
		orderTable.put(order.getOrderId(), order);
	}
	
	public Long getOrderSeq(){
		Long seq = orderSequence;
		orderSequence++;
		return seq;
	}

}
