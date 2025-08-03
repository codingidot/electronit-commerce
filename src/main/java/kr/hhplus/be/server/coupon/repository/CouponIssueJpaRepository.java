package kr.hhplus.be.server.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity ,Long>{
	int countByCouponIdAndUserId(Long couponId, Long userId);
	CouponIssueEntity findByCouponIdAndUserId(Long couponId, Long userId);
}
