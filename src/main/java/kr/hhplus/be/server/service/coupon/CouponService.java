package kr.hhplus.be.server.service.coupon;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
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

	
	public void issueCoupon(CouponIssueRequestDto requestDto) throws Exception {
		int issuedCnt = couponRepository.getIssueData(requestDto.getCouponId());
		int limit = 0;
		
		Optional<CouponDto> info = couponRepository.getCoupon(requestDto.getCouponId());
		if (info.isEmpty()) {
		    throw new Exception("해당 쿠폰은 존재하지 않습니다.");
		}
		CouponDto cp = info.get(); 
		limit = cp.getCount();
		if(issuedCnt >= limit) {
			throw new Exception("해당 쿠폰은 선착순 마감되었습니다.");
		}
		
		Long issueId = couponRepository.getCouponIssueSeq();
		
		couponRepository.issueSave(new CouponIssueDto(issueId,cp.getCouponId() , requestDto.getUserId(), "N"));
	}
}
