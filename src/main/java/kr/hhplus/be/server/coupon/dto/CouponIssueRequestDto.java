package kr.hhplus.be.server.coupon.dto;


public class CouponIssueRequestDto {
	
	private Long userId;
	private Long couponId; 

	public CouponIssueRequestDto() {}
    public CouponIssueRequestDto(Long couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }

	public Long getUserId() {
		return userId;
	}

	public Long getCouponId() {
		return couponId;
	}
    
}
