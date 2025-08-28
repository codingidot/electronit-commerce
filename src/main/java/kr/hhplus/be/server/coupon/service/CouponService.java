package kr.hhplus.be.server.coupon.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
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
	private final StringRedisTemplate redisTemplate;
	
	CouponService(CouponRepository couponRepository,RedissonClient redissonClient,TransactionTemplate transactionTemplate,StringRedisTemplate redisTemplate){
		this.couponRepository = couponRepository; 
		this.redissonClient = redissonClient;
		this.transactionTemplate = transactionTemplate;
		this.redisTemplate = redisTemplate;
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
		String remainKey = "coupon:remain:" + couponId;
		
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 7, TimeUnit.SECONDS)) {
            	
            	 // 1. 캐시에서 남은 개수 조회
                String remainStr = redisTemplate.opsForValue().get(remainKey);
                Long remain = remainStr != null ? Long.parseLong(remainStr) : null;

                if (remain == null) {
                    // 캐시에 없으면 DB 조회
                    Optional<CouponEntity> couponOpt = couponRepository.getCoupon(couponId);
                    if (couponOpt.isEmpty()) {
                        throw new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다. couponId=" + couponId);
                    }
                    CouponEntity couponEntity = couponOpt.get();
                    long remainCnt = couponEntity.getCount() - couponEntity.getIssuedCount();
                    remain = remainCnt;

                    // 캐시에 저장 (TTL 5분 예시)
                    redisTemplate.opsForValue().set(remainKey, String.valueOf(remainCnt), 5, TimeUnit.MINUTES);
                }

                // 2. 선착순 마감 체크
                if (remain <= 0) {
                    throw new IllegalStateException("해당 쿠폰은 선착순 마감되었습니다. couponId=" + couponId);
                }

                // 3. 캐시에서 -1 처리
                Long afterDecr = redisTemplate.opsForValue().decrement(remainKey);
            	
            	transactionTemplate.execute(status -> {
            		try {
            			Optional<CouponEntity> coupon = couponRepository.getCoupon(couponId);//쿠폰정보
                		int userIssueCnt = couponRepository.checkUserCouponHave(couponId, userId);//사용자가 해당 쿠폰을 발급받은 수
                		CouponIssueEntity issueEntity = CouponIssueEntity.toEntity(coupon, userIssueCnt, userId);//쿠폰발급 생성
                		CouponEntity couponEntity = coupon.get();
                		couponEntity.setIssuedCount(couponEntity.getIssuedCount()+1);//쿠폰 발급 수 +1
                		//저장
                		couponRepository.couponSave(couponEntity);
                		couponRepository.issueSave(issueEntity);
            		}catch(Exception e) {
            			status.setRollbackOnly();
            		}finally {
            	        // 롤백이면 캐시 복원 (+1)
            	        if (status.isRollbackOnly()) {
            	        	redisTemplate.opsForValue().increment(remainKey);
            	        }
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
