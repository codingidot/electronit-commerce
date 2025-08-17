package kr.hhplus.be.server.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponIssueDto {

	private Long issueId;
	private Long couponId;
	private Long userId;
	private String useYn;
}
