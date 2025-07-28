package kr.hhplus.be.server.repository.coupon;

import java.util.Optional;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.dto.coupon.CouponIssueDto;

public interface CouponRepository {

	public Optional<Coupon> getCoupon(Long couponId);

	public int getIssueData(Long couponId);

	public void issueSave(CouponIssueDto issueDto);
	
	public Long getCouponIssueSeq();
}
