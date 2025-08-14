package kr.hhplus.be.server.order.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.order.entity.OrderEntity;

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

	@Override
	public Optional<OrderEntity> getOrderInfo(Long goodsId) {
		return orderJpaRepository.findById(goodsId);
	}
}
