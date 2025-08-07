package kr.hhplus.be.server.coupon.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;

@Repository
public class CouponRepositoryImpl implements CouponRepository{

	private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueRepository;

    public CouponRepositoryImpl(CouponJpaRepository couponJpaRepository,
                                  CouponIssueJpaRepository couponIssueRepository) {
        this.couponJpaRepository = couponJpaRepository;
        this.couponIssueRepository = couponIssueRepository;
    }
	
	
	@Override
	public Optional<CouponEntity> getCoupon(Long couponId) {
		Optional<CouponEntity> result = couponJpaRepository.findById(couponId);
		return result;
	}

	@Override
	public int getIssueData(Long couponId) {
		Optional<CouponEntity> coupon = couponJpaRepository.findById(couponId);
		int issuedCnt = 0;
		if(coupon.isPresent()) {
			issuedCnt = coupon.get().getIssuedCount();
		}
		return issuedCnt;
	}
	
	@Override
	public int checkUserCouponHave(Long couponId, Long userId) {
		int count = couponIssueRepository.countByCouponIdAndUserId(couponId, userId);
		return count;
	}

	@Override
	public CouponIssueEntity issueSave(CouponIssueEntity entity) {
		CouponIssueEntity saved = couponIssueRepository.save(entity);
		return saved;
	}
	
	@Override
	public Optional<CouponIssueEntity> getUserIssueData(Long couponId, Long userId) {
		CouponIssueEntity issueData = couponIssueRepository.findByCouponIdAndUserId(couponId, userId);
		return Optional.ofNullable(issueData);
	}


	@Override
	public CouponEntity couponSave(CouponEntity couponentity) {
		CouponEntity entity = couponJpaRepository.save(couponentity);
		return entity;
	}
}
