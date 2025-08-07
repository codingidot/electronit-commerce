package kr.hhplus.be.server.coupon.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;
import kr.hhplus.be.server.coupon.repository.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	private static int MAX_RETRY = 5;
	
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
		int tryCnt = 0;

		while(tryCnt < MAX_RETRY) {
			try {
				this.issueCouponWithTransaction(userId, couponId);
				break;//쿠폰 발급 성공하면 break
			}catch(ObjectOptimisticLockingFailureException e) {
				//낙관적 락 에러 발생시 재시도
				tryCnt++;
				if (tryCnt >= MAX_RETRY) {
                    throw new Exception("해당 쿠폰을 발급하는 사용자가 많아 실패하였습니다. 잠시후 다시 시도해주세요.", e);
                }
				try { Thread.sleep(500); } catch (InterruptedException ignored) {};
			}catch(Exception e) {
				//CouponIssueEntity.toEntity 에서 [쿠폰 존재],[쿠폰 선착순 마감], [이미 발급한 쿠폰] 에러 받기
				//Exception을 던져 발급 중단
				throw e;
			}
		}
	}
	
	//[쿠폰발급 로직 트랜잭션으로 처리]
	//낙관적 락 ObjectOptimisticLockingFailureException이 커밋시점에 발생하면
	//issueCoupon 메소드에서 catch하여 재실행 할 수 있도록 분리
	@Transactional(rollbackFor = Exception.class, isolation= Isolation.READ_COMMITTED)
	public void issueCouponWithTransaction(Long userId, Long couponId) throws Exception {
		Optional<CouponEntity> coupon = couponRepository.getCoupon(couponId);//쿠폰정보
		int userIssueCnt = couponRepository.checkUserCouponHave(couponId, userId);//사용자가 해당 쿠폰을 발급받은 수
		CouponIssueEntity issueEntity = CouponIssueEntity.toEntity(coupon, userIssueCnt, userId);//쿠폰발급 생성
		CouponEntity couponEntity = coupon.get();
		couponEntity.setIssuedCount(couponEntity.getIssuedCount()+1);//쿠폰 발급 수 +1
		//저장
		couponRepository.couponSave(couponEntity);
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
