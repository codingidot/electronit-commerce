package kr.hhplus.be.server.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.entity.order.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long>{

}
