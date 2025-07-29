package kr.hhplus.be.server.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.entity.coupon.CouponEntity;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long>{

	int countByCouponId(Long couponId);
}
