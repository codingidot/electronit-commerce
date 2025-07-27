package kr.hhplus.be.server.dto.coupon;

import java.math.BigDecimal;

public class CouponDto {

	private Long couponId;
    private String couponName;
    private String couponType;
    private BigDecimal couponValue;
    private boolean issueSuccess;
    
	public CouponDto(Long couponId, String couponName, String couponType, BigDecimal couponValue, boolean issueSuccess) {
		this.couponId = couponId;
		this.couponName = couponName;
		this.couponType = couponType;
		this.couponValue = couponValue; 
		this.issueSuccess = issueSuccess;
	}

	public Long getCouponId() {
		return couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public String getCouponType() {
		return couponType;
	}

	public BigDecimal getCouponValue() {
		return couponValue;
	}

	public boolean isIssueSuccess() {
		return issueSuccess;
	}
}
