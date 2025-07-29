package kr.hhplus.be.server.entity.coupon;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.dto.coupon.CouponIssueDto;

public class CouponIssueEntity {
	
	 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    private Long couponId;

    private Long userId;

    private String useYn;

    protected CouponIssueEntity() {
    }

    public CouponIssueEntity(Long couponId, Long userId, String useYn) {
        this.couponId = couponId;
        this.userId = userId;
        this.useYn = useYn;
    }

    public static CouponIssueEntity toEntity(CouponIssueDto dto) {
        return new CouponIssueEntity(
            dto.getCouponId(),
            dto.getUserId(),
            dto.getUseYn()
        );
    }

}
