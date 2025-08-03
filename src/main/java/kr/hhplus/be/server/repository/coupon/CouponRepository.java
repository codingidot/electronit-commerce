package kr.hhplus.be.server.repository.coupon;

import java.util.Optional;

import kr.hhplus.be.server.entity.coupon.CouponEntity;
import kr.hhplus.be.server.entity.coupon.CouponIssueEntity;

public interface CouponRepository {

	public Optional<CouponEntity> getCoupon(Long couponId);

	public int getIssueData(Long couponId);
	
	public CouponIssueEntity getUserIssueData(Long couponId, Long userId);
	
	public CouponIssueEntity issueSave(CouponIssueEntity issueEntity);
	
	public int checkUserCouponHave(Long couponId, Long userId);
}
