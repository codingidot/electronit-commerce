package kr.hhplus.be.server.repository.coupon;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.entity.coupon.CouponEntity;
import kr.hhplus.be.server.entity.coupon.CouponIssueEntity;

@Repository
public class CouponJpaRepositoryImpl implements CouponRepository{

	CouponJpaRepository couponJpaRepository;
	CouponIssueJpaRepository couponIssueRepository;
	
	
	@Override
	public Optional<CouponEntity> getCoupon(Long couponId) {
		Optional<CouponEntity> result = couponJpaRepository.findById(couponId);
		return result;
	}

	@Override
	public int getIssueData(Long couponId) {
		return couponJpaRepository.countByCouponId(couponId);
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
	public CouponIssueEntity getUserIssueData(Long couponId, Long userId) {
		CouponIssueEntity issueData = couponIssueRepository.findByCouponIdAndUserId(couponId, userId);
		return issueData;
	}
}
