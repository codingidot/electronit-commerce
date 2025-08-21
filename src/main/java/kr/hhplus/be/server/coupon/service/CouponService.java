package kr.hhplus.be.server.coupon.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import kr.hhplus.be.server.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;
import kr.hhplus.be.server.coupon.repository.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	private final RedissonClient redissonClient;
	private final TransactionTemplate transactionTemplate;
	
	CouponService(CouponRepository couponRepository,RedissonClient redissonClient,TransactionTemplate transactionTemplate){
		this.couponRepository = couponRepository; 
		this.redissonClient = redissonClient;
		this.transactionTemplate = transactionTemplate;
	}
	
	//쿠폰정보 가져오기
	public Optional<CouponEntity> getCoupon(Long couponId){
		return couponRepository.getCoupon(couponId);
	}

	//쿠폰 발급
	public void issueCoupon(CouponIssueRequest requestDto) throws Exception {
		Long userId = requestDto.getUserId();
		Long couponId = requestDto.getCouponId();
		String lockKey = "lock:coupon:" + couponId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 7, TimeUnit.SECONDS)) {
            	transactionTemplate.execute(status -> {
            		try {
            			Optional<CouponEntity> coupon = couponRepository.getCoupon(couponId);//쿠폰정보
                		int userIssueCnt = couponRepository.checkUserCouponHave(couponId, userId);//사용자가 해당 쿠폰을 발급받은 수
                		CouponIssueEntity issueEntity = CouponIssueEntity.toEntity(coupon, userIssueCnt, userId);//쿠폰발급 생성
                		System.out.println("조회한 쿠폰 아이디 => " + coupon.get().getCouponId());
                		CouponEntity couponEntity = coupon.get();
                		couponEntity.setIssuedCount(couponEntity.getIssuedCount()+1);//쿠폰 발급 수 +1
                		//저장
                		couponRepository.couponSave(couponEntity);
                		couponRepository.issueSave(issueEntity);
            		}catch(Exception e) {
            			status.setRollbackOnly();
            		}
            		
            	    return null;
            	});
            }else {
            	throw new Exception("해당 쿠폰을 발급하려는 사용자가 많습니다. 잠시후 다시 시도해주세요.");     	
            }
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
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
		CouponIssueEntity issueData = couponRepository.getUserIssueData(couponId, userId);
		issueData.setUseYn("Y");
		couponRepository.issueSave(issueData);
	}
}
