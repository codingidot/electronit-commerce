package kr.hhplus.be.server.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponIssueRequest {
	
	private Long userId;
	private Long couponId; 
}
