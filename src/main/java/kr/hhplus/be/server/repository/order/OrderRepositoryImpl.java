package kr.hhplus.be.server.repository.order;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.entity.order.OrderEntity;

@Repository
public class OrderRepositoryImpl implements OrderRepository{

	private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }
	
	@Override
	public void insertOrderInfo(OrderEntity entity) {
		orderJpaRepository.save(entity);
	}
}
