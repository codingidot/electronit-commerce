package kr.hhplus.be.server.coupon.repository;

import java.util.Optional;

import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;

public interface CouponRepository {

	public Optional<CouponEntity> getCoupon(Long couponId);

	public int getIssueData(Long couponId);
	
	public CouponIssueEntity getUserIssueData(Long couponId, Long userId);
	
	public CouponIssueEntity issueSave(CouponIssueEntity issueEntity);
	
	public int checkUserCouponHave(Long couponId, Long userId);
}
