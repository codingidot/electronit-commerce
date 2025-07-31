package kr.hhplus.be.server.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.entity.coupon.CouponIssueEntity;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity ,Long>{
	int countByCouponIdAndUserId(Long couponId, Long userId);
	CouponIssueEntity findByCouponIdAndUserId(Long couponId, Long userId);
}
