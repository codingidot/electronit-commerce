package kr.hhplus.be.server.coupon.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;
import kr.hhplus.be.server.coupon.repository.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	
	CouponService(CouponRepository couponRepository){
		this.couponRepository = couponRepository; 
	}
	
	//쿠폰정보 가져오기
	public Optional<CouponEntity> getCoupon(Long couponId){
		return couponRepository.getCoupon(couponId);
	}

	//쿠폰 발급
	public void issueCoupon(CouponIssueRequestDto requestDto) throws Exception {
		Long userId = requestDto.getUserId();
		Long couponId = requestDto.getCouponId();
		int issuedCnt = couponRepository.getIssueData(couponId);//해당 쿠폰의 총 발급수
		int userIssueCnt = couponRepository.checkUserCouponHave(couponId, userId);
		Optional<CouponEntity> coupon = couponRepository.getCoupon(couponId);

		CouponIssueEntity issueEntity = CouponIssueEntity.toEntity(coupon, issuedCnt, userIssueCnt, userId);
		
		couponRepository.issueSave(issueEntity);
	}

	//쿠폰가격 적용
	public BigDecimal applyDiscount(CouponEntity couponInfo, BigDecimal unitPrice, int buyCnt, Long userId) throws Exception {
		if (couponInfo == null) {
	        throw new IllegalArgumentException("쿠폰 정보가 null입니다.");
	    }
		BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(buyCnt));
		String couponType = couponInfo.getCouponType();// PERCENT : 전체에서 퍼센트 할인, DEDUCT : 금액 차감
		BigDecimal couponVal = couponInfo.getCouponValue();

		if (couponVal == null) {
	        throw new IllegalArgumentException("쿠폰 금액이 null입니다.");
	    }
		
		//쿠폰금액 차감
		if("PERCENT".equals(couponType)) {//퍼센트 할인

			BigDecimal discountRate = couponVal.divide(BigDecimal.valueOf(100));
		    BigDecimal discountAmount = totalPrice.multiply(discountRate);
		    totalPrice = totalPrice.subtract(discountAmount);
		    
		}else if("DEDUCT".equals(couponType)) {//일정 금액 할인
			
			totalPrice = totalPrice.subtract(couponVal);
			if(totalPrice.compareTo(BigDecimal.ZERO) < 0) {
				throw new Exception("쿠폰 할인 금액이 주문 금액보다 크면 안됩니다.");
			}
			
		}
		this.couponUse(couponInfo.getCouponId(), userId);
		
		return totalPrice;
	}
	
	//쿠폰사용처리
	public void couponUse(Long couponId, Long userId) {
		CouponIssueEntity issueData = couponRepository.getUserIssueData(couponId, userId);
		issueData.setUseYn("Y");
		couponRepository.issueSave(issueData);
	}
}
