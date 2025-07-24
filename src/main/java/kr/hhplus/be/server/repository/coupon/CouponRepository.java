package kr.hhplus.be.server.repository.coupon;

import java.util.Optional;

import kr.hhplus.be.server.dto.coupon.CouponDto;

public interface CouponRepository {

	public Optional<CouponDto> getCoupon(Long couponId);
}
