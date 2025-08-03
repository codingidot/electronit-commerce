package kr.hhplus.be.server.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.order.entity.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long>{

}
