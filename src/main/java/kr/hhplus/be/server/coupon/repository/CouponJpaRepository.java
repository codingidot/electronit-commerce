package kr.hhplus.be.server.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.coupon.entity.CouponEntity;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long>{

	int countByCouponId(Long couponId);
	
	Optional<CouponEntity> findById(Long CouponId);

}
