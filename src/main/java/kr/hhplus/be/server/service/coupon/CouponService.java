package kr.hhplus.be.server.service.coupon;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.dto.coupon.CouponIssueDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
import kr.hhplus.be.server.repository.coupon.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	
	CouponService(CouponRepository couponRepository){
		this.couponRepository = couponRepository; 
	}
	
	public Optional<Coupon> getCoupon(Long couponId){
		return couponRepository.getCoupon(couponId);
	}

	//쿠폰 발급
	public void issueCoupon(CouponIssueRequestDto requestDto) throws Exception {
		int issuedCnt = couponRepository.getIssueData(requestDto.getCouponId());
		int limit = 0;
		
		Optional<Coupon> info = couponRepository.getCoupon(requestDto.getCouponId());
		if (info.isEmpty()) {
		    throw new Exception("해당 쿠폰은 존재하지 않습니다.");
		}
		Coupon cp = info.get(); 
		limit = cp.getCount();
		if(issuedCnt >= limit) {
			throw new Exception("해당 쿠폰은 선착순 마감되었습니다.");
		}
		
		Long issueId = couponRepository.getCouponIssueSeq();
		
		couponRepository.issueSave(new CouponIssueDto(issueId,cp.getCouponId() , requestDto.getUserId(), "N"));
	}

	//쿠폰가격 적용
	public BigDecimal applyDiscount(Coupon couponInfo, BigDecimal unitPrice, int buyCnt) throws Exception {
		if (couponInfo == null) {
	        throw new IllegalArgumentException("쿠폰 정보가 null입니다.");
	    }
		BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(buyCnt));
		String couponType = couponInfo.getCouponType();// PERCENT : 전체에서 퍼센트 할인, DEDUCT : 금액 차감
		BigDecimal couponVal = couponInfo.getCouponValue();
		System.out.println(totalPrice);
		if (couponVal == null) {
	        throw new IllegalArgumentException("쿠폰 금액이 null입니다.");
	    }
		System.out.println("sss " + couponVal);
		//쿠폰금액 차감
		if("PERCENT".equals(couponType)) {//퍼센트 할인
			System.out.println("sss " + couponVal);
			
			BigDecimal discountRate = couponVal.divide(BigDecimal.valueOf(100));
		    BigDecimal discountAmount = totalPrice.multiply(discountRate);
		    System.out.println("sss " + discountRate);
		    totalPrice = totalPrice.subtract(discountAmount);
		}else if("DEDUCT".equals(couponType)) {//일정 금액 할인
			totalPrice = totalPrice.subtract(couponVal);
			if(totalPrice.compareTo(BigDecimal.ZERO) < 0) {
				throw new Exception("쿠폰 할인 금액이 주문 금액보다 크면 안됩니다.");
			}
		}
		return totalPrice;
	}
}
