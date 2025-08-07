package kr.hhplus.be.server.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.coupon.entity.CouponEntity;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long>{

	int countByCouponId(Long couponId);
	
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	Optional<CouponEntity> findById(Long CouponId);

}
