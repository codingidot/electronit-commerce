package kr.hhplus.be.server.service.coupon;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.repository.coupon.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	
	CouponService(CouponRepository couponRepository){
		this.couponRepository = couponRepository; 
	}
	
	public Optional<CouponDto> getCoupon(Long couponId){
		return couponRepository.getCoupon(couponId);
	}
}
