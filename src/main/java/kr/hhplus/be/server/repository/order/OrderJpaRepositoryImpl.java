package kr.hhplus.be.server.repository.order;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.entity.order.OrderEntity;

@Repository
public class OrderJpaRepositoryImpl implements OrderRepository{

	OrderJpaRepository orderJpaRepository;
	
	@Override
	public void insertOrderInfo(OrderEntity entity) {
		orderJpaRepository.save(entity);
	}

	@Override
	public Long getOrderSeq() {
		// TODO Auto-generated method stub
		return null;
	}

}
