package kr.hhplus.be.server.service.order;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.dto.order.OrderDto;
import kr.hhplus.be.server.repository.order.OrderRepository;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	OrderService(OrderRepository orderRepository){
		this.orderRepository = orderRepository;
	}

	public void insertOrder(OrderDto orderDto) {
		orderRepository.insertOrderInfo(orderDto);
	}
	
	public Long getOrderSeq() {
		return orderRepository.getOrderSeq();
	}

}
