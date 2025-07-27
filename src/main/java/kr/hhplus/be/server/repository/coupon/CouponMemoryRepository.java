package kr.hhplus.be.server.repository.coupon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import kr.hhplus.be.server.dto.coupon.CouponDto;

public class CouponMemoryRepository implements CouponRepository{

	public Map<Long, CouponDto> couponTable = new HashMap<>();
	
	@Override
	public Optional<CouponDto> getCoupon(Long couponId) {
		
		return Optional.ofNullable(couponTable.get(couponId));
	}

}
