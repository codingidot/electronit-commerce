package kr.hhplus.be.server.dto.coupon;

public class CouponDto {

	private Long couponId;
    private String couponName;
    private String couponType;
    private boolean issueSuccess;
    
	public CouponDto(Long couponId, String couponName, String couponType, boolean issueSuccess) {
		this.couponId = couponId;
		this.couponName = couponName;
		this.couponType = couponType;
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

	public boolean isIssueSuccess() {
		return issueSuccess;
	}
}
