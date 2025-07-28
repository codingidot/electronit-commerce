package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;

public class Coupon {

	private Long couponId;
    private String couponName;
    private String couponType;
    private BigDecimal couponValue;
    private int count;
    private boolean issueSuccess;
    
	public Coupon(Long couponId, String couponName, String couponType, BigDecimal couponValue, boolean issueSuccess, int count) {
		this.couponId = couponId;
		this.couponName = couponName;
		this.couponType = couponType;
		this.couponValue = couponValue; 
		this.issueSuccess = issueSuccess;
		this.count = count;
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
	
	public int getCount() {
		return count;
	}
}
