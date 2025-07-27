package kr.hhplus.be.server.repository.coupon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import kr.hhplus.be.server.dto.coupon.Coupon;
import kr.hhplus.be.server.dto.coupon.CouponIssueDto;

public class CouponMemoryRepository implements CouponRepository{

	private Map<Long, Coupon> couponTable = new HashMap<>();
	private Map<Long, CouponIssueDto> issueTable = new HashMap<>();
	private Long couponSeq = 1L;
	private Long couponIssueSeq = 1L;
	
	@Override
	public Optional<Coupon> getCoupon(Long couponId) {
		
		return Optional.ofNullable(couponTable.get(couponId));
	}

	@Override
	public int getIssueData(Long couponId) {
		int cnt = 0;
		issueTable.forEach(null);
		for (CouponIssueDto coupon : issueTable.values()) {
			if(couponId == coupon.getCouponId()) {
				cnt++;
			}
		}
		return cnt;
	}

	@Override
	public void issueSave(CouponIssueDto couponDto) {
		issueTable.put(couponDto.getIssueId(), couponDto);
	}

	@Override
	public Long getCouponIssueSeq() {
		return couponIssueSeq;
	}


}
