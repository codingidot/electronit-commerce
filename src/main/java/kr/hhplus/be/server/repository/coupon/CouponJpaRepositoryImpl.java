package kr.hhplus.be.server.repository.coupon;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.dto.coupon.CouponIssueDto;
import kr.hhplus.be.server.entity.coupon.CouponEntity;
import kr.hhplus.be.server.entity.coupon.CouponIssueEntity;

@Repository
public class CouponJpaRepositoryImpl implements CouponRepository{

	CouponJpaRepository couponJpaRepository;
	CouponIssueJpaRepository couponIssueRepository;
	
	
	@Override
	public Optional<Coupon> getCoupon(Long couponId) {
		Optional<CouponEntity> result = couponJpaRepository.findById(couponId);
		Coupon cp;
		if(result.isPresent()) {
			CouponEntity entity = result.get();
			cp = entity.toDomain(entity);
			return Optional.ofNullable(cp);
		}
		return Optional.empty();
	}

	@Override
	public int getIssueData(Long couponId) {
		return couponJpaRepository.countByCouponId(couponId);
	}

	@Override
	public void issueSave(CouponIssueDto issueDto) {
		CouponIssueEntity entity = CouponIssueEntity.toEntity(issueDto);
		couponIssueRepository.save(entity);
	}

	@Override
	public Long getCouponIssueSeq() {
		// TODO Auto-generated method stub
		return null;
	}

}
