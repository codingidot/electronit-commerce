package kr.hhplus.be.server.coupon.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;
import kr.hhplus.be.server.coupon.repository.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	private static int MAX_RETRY = 5;
	
	@PersistenceContext
	private EntityManager em;
	
	CouponService(CouponRepository couponRepository){
		this.couponRepository = couponRepository; 
	}
	
	//쿠폰정보 가져오기
	public Optional<CouponEntity> getCoupon(Long couponId){
		return couponRepository.getCoupon(couponId);
	}

	//쿠폰 발급
	@Transactional(rollbackFor = Exception.class, isolation= Isolation.READ_COMMITTED)
	public void issueCoupon(CouponIssueRequestDto requestDto) throws Exception {
		Long userId = requestDto.getUserId();
		Long couponId = requestDto.getCouponId();
		int tryCnt = 0;
		Long test = (long) 0;

		while(tryCnt < MAX_RETRY) {
			try {
				Optional<CouponEntity> coupon = couponRepository.getCoupon(couponId);//쿠폰정보
				int userIssueCnt = couponRepository.checkUserCouponHave(couponId, userId);//사용자가 해당 쿠폰을 발급받은 수
				CouponIssueEntity issueEntity = CouponIssueEntity.toEntity(coupon, userIssueCnt, userId);//쿠폰발급 생성
				CouponEntity couponEntity = coupon.get();
				couponEntity.setIssuedCount(couponEntity.getIssuedCount()+1);//쿠폰 발급 수 +1
				System.out.println("발급된 수 > " +couponEntity.getIssuedCount());
				System.out.println("버전1 > " +couponEntity.getVersion());
				//저장
				couponRepository.couponSave(couponEntity);
				System.out.println("issueEntity1 > " + issueEntity.getCouponId());
				System.out.println("issueEntity2 > " + issueEntity.getUserId());
				System.out.println("issueEntity3 > " + issueEntity.getIssueId());
				System.out.println("발급된 수!!! > " + issueEntity);
				CouponIssueEntity wow = couponRepository.issueSave(issueEntity);
				test= wow.getIssueId();
				System.out.println("버전2 > " +couponEntity.getVersion());
				System.out.println("wow1 > " + wow.getIssueId());
				System.out.println("wow2 > " + wow.getCouponId());
				System.out.println("wow3 > " + wow.getUserId());
				em.flush();
				em.clear();
			}catch(OptimisticLockException | ObjectOptimisticLockingFailureException e) {
				e.printStackTrace();
				System.out.println("낙관적 락 에러 발생");
				tryCnt++;
                if (tryCnt >= MAX_RETRY) {
                    throw new Exception("해당 쿠폰을 발급하는 사용자가 많아 실패하였습니다. 잠시후 다시 시도해주세요.", e);
                }
                try { Thread.sleep(50); } catch (InterruptedException ignored) {};
			} catch (Exception e) {
			    // 그 외 모든 예외 처리
			    e.printStackTrace();
			    System.out.println("일반 예외 발생: " + e.getMessage());
			    throw e; // 또는 다시 재시도 로직
			}
			System.out.println("test!!! " + test);
			System.out.println("끝에 도달");
			
			
		}
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
		Optional<CouponIssueEntity> issueData = couponRepository.getUserIssueData(couponId, userId);
		if(issueData.isPresent()) {
			issueData.get().setUseYn("Y");
			couponRepository.issueSave(issueData.get());
		}
	}
}
