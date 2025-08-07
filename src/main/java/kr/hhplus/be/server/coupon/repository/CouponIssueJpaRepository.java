package kr.hhplus.be.server.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity ,Long>{
	@Query("select count(cie1_0.issueId) from CouponIssueEntity  cie1_0 where cie1_0.couponId=:couponId and cie1_0.userId=:userId")
	int countByCouponIdAndUserId(@Param("couponId") Long couponId,@Param("userId") Long userId);
	CouponIssueEntity findByCouponIdAndUserId(Long couponId, Long userId);
	int countByCouponId(Long couponId);
}
