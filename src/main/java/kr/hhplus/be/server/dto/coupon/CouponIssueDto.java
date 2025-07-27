package kr.hhplus.be.server.dto.coupon;

public class CouponIssueDto {

	private Long issueId;
	private Long couponId;
	private Long userId;
	private String useYn;
	
	
	public CouponIssueDto(Long issueId, Long couponId, Long userId, String useYn) {
		this.issueId = issueId;
		this.couponId = couponId;
		this.userId = userId;
		this.useYn = useYn;
	}
	public Long getIssueId() {
		return issueId;
	}
	public Long getCouponId() {
		return couponId;
	}
	public Long getUserId() {
		return userId;
	}
	public String getUseYn() {
		return useYn;
	}
	
	
	
}
